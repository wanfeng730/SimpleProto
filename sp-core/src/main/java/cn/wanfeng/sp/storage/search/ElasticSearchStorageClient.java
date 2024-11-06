package cn.wanfeng.sp.storage.search;


import cn.wanfeng.proto.constants.SpExceptionMessage;
import cn.wanfeng.proto.exception.SpException;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.indices.PutMappingRequest;
import co.elastic.clients.util.ObjectBuilder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * @date: 2024-11-06 21:37
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Component
public class ElasticSearchStorageClient implements SearchStorageClient{

    @Resource
    private ElasticsearchClient client;

    @Override
    public void insertObject(String tableName, Map<String, Object> objectData) {

    }

    @Override
    public void updateObject(String tableName, Map<String, Object> objectData) {

    }

    @Override
    public void removeObject(String tableName, Long id) {

    }

    /**
     * 根据属性名和值自动创建mapping
     * @param fieldName 属性名
     * @param value 值
     */
    private void autoCreateMappingByValue(String fieldName, Object value){
        try {
            client.indices().putMapping(PutMappingRequest.of(req -> req
                    .properties(fieldName, builder -> handlePropertyBuilderByValue(builder, value))
            ));
        } catch (IOException e) {
            throw new SpException(SpExceptionMessage.autoCreateMappingError(fieldName, value), e);
        }
    }

    private static ObjectBuilder<Property> handlePropertyBuilderByValue(Property.Builder builder, Object value){
        Class<?> valueClass = value.getClass();
        if(valueClass == String.class){
            return builder.text(t -> t);
        }
        if(valueClass == Integer.class || valueClass == Long.class){
            return builder.long_(t -> t);
        }
        if(valueClass == Boolean.class){
            return builder.boolean_(t -> t);
        }
        if(valueClass == Date.class || valueClass == LocalDate.class || valueClass == LocalDateTime.class){
            return builder.date(t -> t);
        }
        return null;
    }
}
