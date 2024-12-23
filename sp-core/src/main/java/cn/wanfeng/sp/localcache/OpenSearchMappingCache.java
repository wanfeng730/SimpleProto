package cn.wanfeng.sp.localcache;


import cn.wanfeng.sp.exception.SpSearchStorageException;
import org.apache.commons.collections4.MapUtils;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch.indices.GetMappingResponse;
import org.opensearch.client.opensearch.indices.get_mapping.IndexMappingRecord;

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

    private static final Map<String, Property.Kind> propertyKindCache = new ConcurrentHashMap<>(32);

    public static boolean checkFieldExistInCache(String fieldName){
        return propertyKindCache.containsKey(fieldName);
    }

    public static void putFieldMappingToCache(String fieldName, Property.Kind kind){
        propertyKindCache.put(fieldName, kind);
    }

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
            return;
        }
        propertyKindCache.clear();
        for (Map.Entry<String, Property> propertyEntry : propertyMap.entrySet()) {
            String fieldName = propertyEntry.getKey();
            Property property = propertyEntry.getValue();
            putFieldMappingToCache(fieldName, property._kind());
        }
    }
}
