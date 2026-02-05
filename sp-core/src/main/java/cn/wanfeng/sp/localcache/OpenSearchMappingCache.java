package cn.wanfeng.sp.localcache;


import cn.wanfeng.sp.exception.SpSearchStorageException;
import cn.wanfeng.sp.util.LogUtil;
import org.apache.commons.collections4.MapUtils;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch.indices.GetMappingResponse;
import org.opensearch.client.opensearch.indices.get_mapping.IndexMappingRecord;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date: 2024-12-23 22:53
 * @author: luozh.wanfeng
 * @description: OpenSearch mapping 缓存
 * @since: 1.0
 */
public class OpenSearchMappingCache {

    private static final Logger log = LogUtil.getSimpleProtoLogger();

    private static final Map<String, Map<String, Property.Kind>> indexPropertyKindCache = new ConcurrentHashMap<>(32);

    public static boolean checkFieldExistInCache(String indexName, String fieldName, Property.Kind kind) {
        Map<String, Property.Kind> propertyKindCache = getPropertyKindCacheByIndexName(indexName);
        return propertyKindCache.containsKey(fieldName) && propertyKindCache.get(fieldName) == kind;
    }

    public static boolean checkFieldExistInCache(String indexName, String fieldName) {
        Map<String, Property.Kind> propertyKindCache = getPropertyKindCacheByIndexName(indexName);
        return propertyKindCache.containsKey(fieldName);
    }

    public static void putFieldMappingToCache(String indexName, String fieldName, Property.Kind kind){
        Map<String, Property.Kind> propertyKindCache = getPropertyKindCacheByIndexName(indexName);
        propertyKindCache.put(fieldName, kind);
    }

    public static Map<String, Property.Kind> getPropertyKindCacheByIndexName(String indexName){
        if(!indexPropertyKindCache.containsKey(indexName)){
            indexPropertyKindCache.put(indexName, new ConcurrentHashMap<>(32));
        }
        return indexPropertyKindCache.get(indexName);
    }

    /**
     * 从opensearch中获取某个索引的mapping配置
     * @param openSearchClient 客户端
     * @param indexName 索引名
     */
    public static void syncFieldMappingFromOpenSearch(OpenSearchClient openSearchClient, String indexName){
        GetMappingResponse getMappingResponse;
        try {
            getMappingResponse = openSearchClient.indices().getMapping(req -> req.index(indexName));
        } catch (IOException e) {
            throw new SpSearchStorageException(e, "获取mapping异常[index=%s]", indexName);
        }
        Map<String, IndexMappingRecord> indexMappingRecordMap = getMappingResponse.result();
        Map<String, Property> propertyMap = indexMappingRecordMap.get(indexName).mappings().properties();
        if(MapUtils.isEmpty(propertyMap)){
            log.debug("索引[{}]中没有mapping", indexName);
            return;
        }
        Map<String, Property.Kind> propertyKindCache = getPropertyKindCacheByIndexName(indexName);
        propertyKindCache.clear();
        for (Map.Entry<String, Property> propertyEntry : propertyMap.entrySet()) {
            String fieldName = propertyEntry.getKey();
            Property property = propertyEntry.getValue();
            putFieldMappingToCache(indexName, fieldName, property._kind());
        }
        log.debug("已从索引[{}]获取{}条Mapping加入缓存", indexName, propertyKindCache.size());
    }
}
