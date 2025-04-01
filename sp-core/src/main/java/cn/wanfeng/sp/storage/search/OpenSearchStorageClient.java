package cn.wanfeng.sp.storage.search;


import cn.wanfeng.proto.constants.SpExceptionMessage;
import cn.wanfeng.sp.elastic.ElasticDateTimePattern;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.localcache.OpenSearchMappingCache;
import jakarta.annotation.Resource;
import org.opensearch.client.opensearch.OpenSearchClient;
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
import org.opensearch.client.util.ObjectBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

    @Resource
    private OpenSearchClient openSearchClient;


    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(ElasticDateTimePattern.DATE_TIME_MILLIS.toPattern());
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(ElasticDateTimePattern.DATE_TIME_MILLIS.toPattern());


    @Override
    public void insertObject(String tableName, Map<String, Object> objectData) throws Exception {
        //根据参数类型自动创建mapping
        autoAdaptCreateMappingByObjectData(tableName, objectData);
        //将日期的值更换为格式化字符串，以便es查看
        Map<String, Object> dataMap = generateSearchDataDocument(objectData);

        String id = String.valueOf(dataMap.get(OBJECT_ID_KEY));
        IndexRequest<Map<String, Object>> indexRequest = IndexRequest.of(b -> b.index(tableName).id(id).document(dataMap));
        openSearchClient.index(indexRequest);
    }

    @Override
    public void updateObject(String tableName, Map<String, Object> objectData) throws Exception {
        //将日期的值更换为格式化字符串，以便es查看
        Map<String, Object> dataMap = generateSearchDataDocument(objectData);

        String id = String.valueOf(dataMap.get(OBJECT_ID_KEY));
        UpdateRequest<Map, Map> updateRequest = UpdateRequest.of(req -> req.index(tableName).id(id).doc(dataMap));
        openSearchClient.update(updateRequest, Map.class);
    }

    @Override
    public void removeObject(String tableName, Long id) throws  Exception{
        DeleteRequest deleteRequest = DeleteRequest.of(req -> req.index(tableName).id(String.valueOf(id)));
        openSearchClient.delete(deleteRequest);
    }

    /**
     * 批量新建对象数据
     *
     * @param tableName      对象数据表名、索引
     * @param objectDataList 对象数据列表
     */
    @Override
    public void bulkInsertObject(String tableName, List<Map<String, Object>> objectDataList) throws IOException {
        List<BulkOperation> bulkOperationList = new ArrayList<>();
        for (Map<String, Object> objectData : objectDataList) {
            Long id = (Long) objectData.get(OBJECT_ID_KEY);
            LinkedHashMap<String, Object> document = generateSearchDataDocument(objectData);
            CreateOperation<Map<String, Object>> createOperation = CreateOperation.of(b -> b.index(tableName).id(String.valueOf(id)).document(document));
            BulkOperation bulkOperation = BulkOperation.of(b -> b.create(createOperation));
            bulkOperationList.add(bulkOperation);
        }
        BulkRequest bulkRequest = BulkRequest.of(builder -> builder.index(tableName).operations(bulkOperationList));
        openSearchClient.bulk(bulkRequest);
    }

    /**
     * 批量更新对象数据
     *
     * @param tableName      对象数据表名、索引
     * @param objectDataList 对象数据列表
     */
    @Override
    public void bulkUpdateObject(String tableName, List<Map<String, Object>> objectDataList) throws IOException {
        List<BulkOperation> bulkOperationList = new ArrayList<>();
        for (Map<String, Object> objectData : objectDataList) {
            Long id = (Long) objectData.get(OBJECT_ID_KEY);
            LinkedHashMap<String, Object> document = generateSearchDataDocument(objectData);
            UpdateOperation<Map<String, Object>> updateOperation = UpdateOperation.of(b -> b.index(tableName).id(String.valueOf(id)).document(document));
            BulkOperation bulkOperation = BulkOperation.of(b -> b.update(updateOperation));
            bulkOperationList.add(bulkOperation);
        }
        BulkRequest bulkRequest = BulkRequest.of(builder -> builder.index(tableName).operations(bulkOperationList));
        openSearchClient.bulk(bulkRequest);
    }

    /**
     * 批量删除对象数据
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
        BulkRequest bulkRequest = BulkRequest.of(builder -> builder.index(tableName).operations(bulkOperationList));
        openSearchClient.bulk(bulkRequest);
    }

    /**
     * 根据属性名和值自动创建mapping
     */
    private void autoAdaptCreateMappingByObjectData(String tableName, Map<String, Object> objectData){
        boolean needCreateMapping = false;

        PutMappingRequest.Builder requestBuilder = new PutMappingRequest.Builder();
        requestBuilder.index(tableName);

        for (Map.Entry<String, Object> entry : objectData.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();
            //缓存中已存在，则不用新增该mapping
            if(OpenSearchMappingCache.checkFieldExistInCache(fieldName)){
                continue;
            }
            requestBuilder.properties(fieldName, ob -> handlePropertyBuilderByValue(ob, value));
            needCreateMapping = true;
        }

        if(needCreateMapping){
            boolean acknowledged = false;
            try {
                PutMappingRequest request = requestBuilder.build();
                acknowledged = openSearchClient.indices().putMapping(request).acknowledged();
            } catch (IOException e) {
                throw new SpException(SpExceptionMessage.AUTO_CREATE_MAPPING_ERROR, e);
            }
        }
    }

    /**
     * 根据值的类型构建默认的mapping配置
     * @param builder mapping
     * @param value 值
     */
    private static ObjectBuilder<Property> handlePropertyBuilderByValue(Property.Builder builder, Object value){
        Class<?> valueClass = value.getClass();
        if(valueClass == String.class){
            return builder.keyword(t -> t);
        }
        if(valueClass == Integer.class || valueClass == Long.class){
            return builder.long_(t -> t);
        }
        if(valueClass == Boolean.class){
            return builder.boolean_(t -> t);
        }
        if(valueClass == Date.class || valueClass == LocalDate.class || valueClass == LocalDateTime.class){
            return builder.date(t -> t.format(DEFAULT_DATE_FORMAT));
        }
        return null;
    }

    /**
     * 将日期数据转换为yyyy-MM-dd HH:mm:ss.SSS进行es创建，以便Kibana显示对应的日期格式
     */
    private static LinkedHashMap<String, Object> generateSearchDataDocument(Map<String, Object> objectData){
        LinkedHashMap<String, Object> convertData = new LinkedHashMap<>();
        for (String key : objectData.keySet()) {
            Object value = objectData.get(key);
            Class<?> valueClass = value.getClass();
            if (valueClass == Date.class) {
                String formatDate = SIMPLE_DATE_FORMAT.format((Date) objectData.get(key));
                convertData.put(key, formatDate);
                continue;
            }
            if (valueClass == LocalDateTime.class) {
                String formatDate = DATE_TIME_FORMATTER.format((LocalDateTime) objectData.get(key));
                convertData.put(key, formatDate);
                continue;
            }
            if (valueClass == LocalDate.class) {
                String formatDate = DATE_TIME_FORMATTER.format((LocalDate) value);
                convertData.put(key, formatDate);
                continue;
            }
            convertData.put(key, value);
        }
        return convertData;
    }

}
