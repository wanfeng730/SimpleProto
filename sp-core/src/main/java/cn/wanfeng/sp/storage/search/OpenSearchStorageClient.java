package cn.wanfeng.sp.storage.search;


import cn.wanfeng.sp.elastic.ElasticDateTimePattern;
import jakarta.annotation.Resource;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private static final String DEFAULT_DATE_FORMAT = ElasticDateTimePattern.DATE_TIME_MILLIS.toPattern() + "||" + ElasticDateTimePattern.EPOCH_SECOND.toPattern();

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(ElasticDateTimePattern.DATE_TIME_MILLIS.toPattern());
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(ElasticDateTimePattern.DATE_TIME_MILLIS.toPattern());

    @Override
    public void insertObject(String tableName, Map<String, Object> objectData) throws Exception {
        //根据参数类型自动创建mapping
        autoCreateMappingByObjectData(tableName, objectData);
        //将日期的值更换为格式化字符串，以便es查看
        Map<String, Object> dataMap = convertDateValueToDateTimeMillis(objectData);

        String id = String.valueOf(dataMap.get(OBJECT_ID_KEY));
        // LzhTODO: opensearch重构
    }

    @Override
    public void updateObject(String tableName, Map<String, Object> objectData) throws Exception {
        //根据参数类型自动创建mapping
        autoCreateMappingByObjectData(tableName, objectData);
        //将日期的值更换为格式化字符串，以便es查看
        Map<String, Object> dataMap = convertDateValueToDateTimeMillis(objectData);

        String id = String.valueOf(dataMap.get(OBJECT_ID_KEY));
        // LzhTODO: opensearch重构
    }

    @Override
    public void removeObject(String tableName, Long id) throws  Exception{
        // LzhTODO: opensearch重构
    }

    /**
     * 根据属性名和值自动创建mapping
     */
    private void autoCreateMappingByObjectData(String tableName, Map<String, Object> objectData){
        // try {
        //     PutMappingRequest request = PutMappingRequest.of(req -> {
        //         //指定索引
        //         req.index(tableName);
        //         //根据数据类型创建合适的mapping
        //         for (Map.Entry<String, Object> data : objectData.entrySet()) {
        //             String fieldName = data.getKey();
        //             Object value = data.getValue();
        //             req.properties(fieldName, builder -> handlePropertyBuilderByValue(builder, value));
        //         }
        //         return req;
        //     });
        //     client.indices().putMapping(request);
        // } catch (IOException e) {
        //     throw new SpException(SpExceptionMessage.AUTO_CREATE_MAPPING_ERROR, e);
        // }
    }

    /**
     * 根据值的类型构建默认的mapping配置
     * @param builder mapping
     * @param value 值
     */
    // private static ObjectBuilder<Property> handlePropertyBuilderByValue(Property.Builder builder, Object value){
    //     // LzhTODO: opensearch重构
    // }

    /**
     * 将日期数据转换为yyyy-MM-dd HH:mm:ss.SSS进行es创建，以便Kibana显示对应的日期格式
     */
    private static Map<String, Object> convertDateValueToDateTimeMillis(Map<String, Object> objectData){
        Map<String, Object> convertData = new HashMap<>();
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
