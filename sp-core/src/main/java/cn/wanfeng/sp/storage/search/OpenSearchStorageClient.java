package cn.wanfeng.sp.storage.search;


import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.api.domain.ISpBaseObject;
import cn.wanfeng.sp.api.model.SpPropertyValue;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.elastic.ElasticDateTimePattern;
import cn.wanfeng.sp.exception.SimpleExceptionCode;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.localcache.OpenSearchMappingCache;
import cn.wanfeng.sp.util.LogUtil;
import cn.wanfeng.sp.util.SimpleReflectUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.annotation.Resource;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch._types.Time;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.DeleteRequest;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.UpdateRequest;
import org.opensearch.client.opensearch.core.bulk.BulkOperation;
import org.opensearch.client.opensearch.core.bulk.CreateOperation;
import org.opensearch.client.opensearch.core.bulk.DeleteOperation;
import org.opensearch.client.opensearch.core.bulk.UpdateOperation;
import org.opensearch.client.opensearch.indices.PutMappingRequest;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.opensearch.client.util.ObjectBuilder;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @date: 2024-11-06 21:37
 * @author: luozh.wanfeng
 * @description: ElasticSearch存储
 * @since: 1.0
 */
@Component
public class OpenSearchStorageClient implements SearchStorageClient{

    private static final Logger log = LogUtil.getSimpleProtoLogger();

    @Resource
    private OpenSearchClient openSearchClient;

    @Resource
    private SimpleProtoConfig simpleProtoConfig;


    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(ElasticDateTimePattern.DATE_TIME_MILLIS.toPattern());
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(ElasticDateTimePattern.DATE_TIME_MILLIS.toPattern());

    private static final Time requestTimeout = Time.of(builder -> builder.time(SimpleProtoConfig.opensearchRequestTimeout + "ms"));

    @Override
    public void createIndexIfNotExist(String tableName) {
        try {
            // 索引是否存在,不存在则创建
            BooleanResponse response = openSearchClient.indices().exists(e -> e.index(tableName));
            if (!response.value()) {
                openSearchClient.indices().create(c -> c.index(tableName));
                log.debug("创建OpenSearch索引[{}]", tableName);
            }
        } catch (IOException e) {
            throw new SpException(e, "创建OpenSearch索引[%s]失败", SimpleProtoConfig.dataTable);
        }
    }

    /**
     * 新增对象
     * 设置请求refresh参数为wait_for（opensearch存在每隔1秒才刷新索引数据的机制，需要等待刷新后再返回请求，保证数据能被正常读取）
     *
     * @param tableName 对象数据表名、索引
     * @param propertyValueContainer 对象数据
     */
    @Override
    public void insertObject(String tableName, Map<String, SpPropertyValue> propertyValueContainer) throws Exception {
        insertObject(tableName, propertyValueContainer, true);
    }

    /**
     * 新建对象数据
     *
     * @param tableName    对象数据表名、索引
     * @param propertyValueContainer   对象数据
     * @param checkMapping 是否检查mapping字段是否存在
     */
    @Override
    public void insertObject(String tableName, Map<String, SpPropertyValue> propertyValueContainer, boolean checkMapping) throws Exception {
        //根据参数类型自动创建mapping
        if(checkMapping){
            autoAdaptCreateMappingByObjectData(tableName, propertyValueContainer);
        }
        //转换为保存opensearch的map
        Map<String, Object> document = convertPropertyValueContainerToDocument(propertyValueContainer);
        //将日期的值更换为格式化字符串，以便es查看
        convertDateValue(document);

        String id = String.valueOf(document.get(OBJECT_ID_KEY));
        IndexRequest<Map<String, Object>> indexRequest = IndexRequest.of(b -> b
                .index(tableName)
                .timeout(requestTimeout)
                .id(id)
                .document(document)
                .refresh(Refresh.WaitFor)
        );
        openSearchClient.index(indexRequest);
    }

    /**
     * 更新对象
     * 设置请求refresh参数为wait_for（opensearch存在每隔1秒才刷新索引数据的机制，需要等待刷新后再返回请求，保证数据能被正常读取）
     *
     * @param tableName 对象数据表名、索引
     * @param propertyValueContainer 对象数据
     */
    @Override
    public void updateObject(String tableName, Map<String, SpPropertyValue> propertyValueContainer) throws Exception {
        //转换为保存opensearch的map
        Map<String, Object> document = convertPropertyValueContainerToDocument(propertyValueContainer);
        //将日期的值更换为格式化字符串，以便es查看
        convertDateValue(document);

        String id = String.valueOf(document.get(OBJECT_ID_KEY));
        UpdateRequest<Map, Map> updateRequest = UpdateRequest.of(builder -> builder
                .index(tableName)
                .timeout(requestTimeout)
                .id(id)
                .doc(document)
                .refresh(Refresh.WaitFor)
        );
        openSearchClient.update(updateRequest, Map.class);
    }

    /**
     * 删除对象
     * 设置请求refresh参数为wait_for（opensearch存在每隔1秒才刷新索引数据的机制，需要等待刷新后再返回请求，保证数据能被正常读取）
     *
     * @param tableName 对象数据表名、索引
     * @param id 对象id
     */
    @Override
    public void removeObject(String tableName, Long id) throws  Exception{
        DeleteRequest deleteRequest = DeleteRequest.of(req -> req
                .index(tableName)
                .timeout(requestTimeout)
                .id(String.valueOf(id))
                .refresh(Refresh.WaitFor)
        );
        openSearchClient.delete(deleteRequest);
    }

    /**
     * 批量新建对象数据
     * 设置请求refresh参数为wait_for（opensearch存在每隔1秒才刷新索引数据的机制，需要等待刷新后再返回请求，保证数据能被正常读取）
     *
     * @param tableName      对象数据表名、索引
     * @param propertyValueContainerList 对象数据列表
     */
    @Override
    public void bulkInsertObject(String tableName, List<Map<String, SpPropertyValue>> propertyValueContainerList) throws IOException {
        List<BulkOperation> bulkOperationList = new ArrayList<>();
        for (Map<String, SpPropertyValue> propertyValueContainer : propertyValueContainerList) {
            Long id = (Long) propertyValueContainer.get(OBJECT_ID_KEY).getValue();
            //转换为保存opensearch的map
            autoAdaptCreateMappingByObjectData(tableName, propertyValueContainer);
            Map<String, Object> document = convertPropertyValueContainerToDocument(propertyValueContainer);
            convertDateValue(document);

            CreateOperation<Map<String, Object>> createOperation = CreateOperation.of(b -> b.index(tableName).id(String.valueOf(id)).document(document));
            BulkOperation bulkOperation = BulkOperation.of(b -> b.create(createOperation));
            bulkOperationList.add(bulkOperation);
        }
        BulkRequest bulkRequest = BulkRequest.of(builder -> builder
                .index(tableName)
                .timeout(requestTimeout)
                .operations(bulkOperationList)
                .refresh(Refresh.WaitFor)
        );
        openSearchClient.bulk(bulkRequest);
    }

    /**
     * 批量更新对象数据
     * 设置请求refresh参数为wait_for（opensearch存在每隔1秒才刷新索引数据的机制，需要等待刷新后再返回请求，保证数据能被正常读取）
     *
     * @param tableName      对象数据表名、索引
     * @param propertyValueContainerList 对象数据列表
     */
    @Override
    public void bulkUpdateObject(String tableName, List<Map<String, SpPropertyValue>> propertyValueContainerList) throws IOException {
        List<BulkOperation> bulkOperationList = new ArrayList<>();
        for (Map<String, SpPropertyValue> propertyValueContainer : propertyValueContainerList) {
            Long id = (Long) propertyValueContainer.get(OBJECT_ID_KEY).getValue();
            //转换为保存opensearch的map
            Map<String, Object> document = convertPropertyValueContainerToDocument(propertyValueContainer);
            convertDateValue(document);

            UpdateOperation<Map<String, Object>> updateOperation = UpdateOperation.of(b -> b.index(tableName).id(String.valueOf(id)).document(document));
            BulkOperation bulkOperation = BulkOperation.of(b -> b.update(updateOperation));
            bulkOperationList.add(bulkOperation);
        }
        BulkRequest bulkRequest = BulkRequest.of(builder -> builder
                .index(tableName)
                .timeout(requestTimeout)
                .operations(bulkOperationList)
                .refresh(Refresh.WaitFor)
        );
        openSearchClient.bulk(bulkRequest);
    }

    /**
     * 批量删除对象数据
     * 设置请求refresh参数为wait_for（opensearch存在每隔1秒才刷新索引数据的机制，需要等待刷新后再返回请求，保证数据能被正常读取）
     *
     * @param tableName 对象数据表名、索引
     * @param idList    对象id列表
     */
    @Override
    public void bulkRemoveObject(String tableName, List<Long> idList) throws IOException {
        List<BulkOperation> bulkOperationList = new ArrayList<>();
        for (Long id : idList) {
            DeleteOperation deleteOperation = DeleteOperation.of(b -> b.index(tableName).id(String.valueOf(id)));
            BulkOperation bulkOperation = BulkOperation.of(b -> b.delete(deleteOperation));
            bulkOperationList.add(bulkOperation);
        }
        BulkRequest bulkRequest = BulkRequest.of(builder -> builder
                .index(tableName)
                .timeout(requestTimeout)
                .operations(bulkOperationList)
                .refresh(Refresh.WaitFor)
        );
        openSearchClient.bulk(bulkRequest);
    }

    /**
     * 将某个类中的ProtoField注解字段的类型同步更到mapping
     *
     * @param tableName 对象数据表名、索引
     * @param clazz     类
     */
    @Override
    public void syncProtoFieldToMapping(String tableName, Class<? extends ISpBaseObject> clazz) {
        List<Field> protoFieldList = Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(ProtoField.class)).toList();
        Map<String, SpPropertyValue> emptyData = new LinkedHashMap<>();
        for (Field field : protoFieldList) {
            String protoFieldName = field.getAnnotation(ProtoField.class).name();
            Class<?> fieldTypeClass = field.getType();
            //如果是枚举类，替换成枚举类中定义的ProtoEnumValue方法返回的类型
            if(fieldTypeClass.isEnum()){
                Method enumValueMethod = SimpleReflectUtils.getProtoEnumValueMethod(fieldTypeClass);
                assert enumValueMethod != null;
                fieldTypeClass = enumValueMethod.getReturnType();
            }
            emptyData.put(protoFieldName, SpPropertyValue.build(fieldTypeClass, null));
        }
        autoAdaptCreateMappingByObjectData(tableName, emptyData);
    }

    /**
     * 将某个类中的TableField、TableId注解字段同步到mapping
     *
     * @param tableName 对象数据表名、索引
     * @param clazz     类
     */
    @Override
    public void syncTableFieldToMapping(String tableName, Class<?> clazz, boolean updateMappingCache) {
        Map<String, SpPropertyValue> emptyData = new LinkedHashMap<>();

        List<Field> tableFieldList = Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(TableField.class)).toList();
        for (Field field : tableFieldList) {
            String fieldName = field.getAnnotation(TableField.class).value();
            Class<?> fieldTypeClass = field.getType();
            emptyData.put(fieldName, SpPropertyValue.build(fieldTypeClass, null));
        }

        List<Field> tableIdList = Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(TableId.class)).toList();
        for (Field field : tableIdList) {
            String fieldName = field.getAnnotation(TableId.class).value();
            Class<?> fieldTypeClass = field.getType();
            emptyData.put(fieldName, SpPropertyValue.build(fieldTypeClass, null));
        }
        autoAdaptCreateMappingByObjectData(tableName, emptyData, updateMappingCache);
    }

    /**
     * 根据属性名和值自动创建mapping
     */
    private void autoAdaptCreateMappingByObjectData(String tableName, Map<String, SpPropertyValue> objectData){
        autoAdaptCreateMappingByObjectData(tableName, objectData, true);
    }

    /**
     * 根据属性名和值自动创建mapping
     */
    private void autoAdaptCreateMappingByObjectData(String tableName, Map<String, SpPropertyValue> objectData, boolean updateMappingCache){
        boolean needCreateMapping = false;

        PutMappingRequest.Builder requestBuilder = new PutMappingRequest.Builder();
        requestBuilder.index(tableName);

        for (Map.Entry<String, SpPropertyValue> entry : objectData.entrySet()) {
            String fieldName = entry.getKey();
            Class<?> fieldClass = entry.getValue().getClazz();
            //缓存中已存在，则不用新增该mapping
            if(OpenSearchMappingCache.checkFieldExistInCache(tableName, fieldName)){
                continue;
            }
            requestBuilder.properties(fieldName, ob -> handlePropertyBuilderByClass(fieldName, ob, fieldClass));

            needCreateMapping = true;
        }

        if(needCreateMapping){
            boolean acknowledged = false;
            try {
                PutMappingRequest request = requestBuilder.build();
                acknowledged = openSearchClient.indices().putMapping(request).acknowledged();
                if(acknowledged && updateMappingCache){
                    log.debug("新增索引[{}]的Mapping: {}", tableName, request.properties().keySet());
                    //成功新增mapping后重新读取最新mapping到缓存中
                    OpenSearchMappingCache.syncFieldMappingFromOpenSearch(openSearchClient, tableName);
                    log.debug("更新索引[{}]的Mapping缓存 数量：{}", tableName, OpenSearchMappingCache.getPropertyKindCacheByIndexName(tableName).size());
                }
            } catch (IOException e) {
                throw new SpException(e, "自动创建Mapping出现未知异常");
            }
        }
    }

    private static ObjectBuilder<Property> handlePropertyBuilderByClass(String fieldName, Property.Builder builder, Class<?> clazz){
        if(clazz == String.class){
            // 字段名以text或json结尾的字段用text类型存储
            if(fieldName.endsWith("json") || fieldName.endsWith("text")){
                return  builder.text(t -> t);
            }else{
                return builder.keyword(t -> t);
            }
        }
        if(clazz == Integer.class || clazz == Long.class){
            return builder.long_(t -> t);
        }
        if(clazz == Boolean.class){
            return builder.boolean_(t -> t);
        }
        if(clazz == Date.class || clazz == LocalDate.class || clazz == LocalDateTime.class){
            return builder.date(t -> t.format(DEFAULT_DATE_FORMAT));
        }
        if(clazz == Double.class){
            return builder.double_(t -> t);
        }
        throw new SpException(SimpleExceptionCode.AUTO_ADAPT_CREATE_MAPPING_NO_MATCH_CLASS, clazz.getName());
    }


    private static Map<String, Object> convertPropertyValueContainerToDocument(Map<String, SpPropertyValue> propertyValueContainer){
        Map<String, Object> document = new LinkedHashMap<>(propertyValueContainer.size());
        for (Map.Entry<String, SpPropertyValue> entry : propertyValueContainer.entrySet()) {
            String fieldName = entry.getKey();
            SpPropertyValue spPropertyValue = entry.getValue();
            document.put(fieldName, spPropertyValue.getValue());
        }
        return document;
    }

    /**
     * 将日期数据转换为yyyy-MM-dd HH:mm:ss.SSS进行es创建，以便Kibana显示对应的日期格式
     */
    private static void convertDateValue(Map<String, Object> objectData){
        for (String key : objectData.keySet()) {
            Object value = objectData.get(key);
            if(Objects.isNull(value)){
                continue;
            }
            Class<?> valueClass = value.getClass();
            if (valueClass == Date.class) {
                String formatDate = SIMPLE_DATE_FORMAT.format((Date) objectData.get(key));
                objectData.put(key, formatDate);
                continue;
            }
            if (valueClass == LocalDateTime.class) {
                String formatDate = DATE_TIME_FORMATTER.format((LocalDateTime) objectData.get(key));
                objectData.put(key, formatDate);
                continue;
            }
            if (valueClass == LocalDate.class) {
                String formatDate = DATE_TIME_FORMATTER.format((LocalDate) value);
                objectData.put(key, formatDate);
                continue;
            }
        }
    }

}
