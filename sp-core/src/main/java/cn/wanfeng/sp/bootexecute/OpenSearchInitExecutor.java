package cn.wanfeng.sp.bootexecute;


import cn.wanfeng.sp.api.domain.ISpBaseObject;
import cn.wanfeng.sp.api.domain.ISpFile;
import cn.wanfeng.sp.api.domain.ISpSysObject;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SpSearchStorageException;
import cn.wanfeng.sp.localcache.OpenSearchMappingCache;
import cn.wanfeng.sp.storage.search.SearchStorageClient;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.indices.PutMappingRequest;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @date: 2024-11-07 21:20
 * @author: luozh.wanfeng
 * @description: 启动初始化OpenSearch数据
 * @since: 1.0
 */
@Component
public class OpenSearchInitExecutor {

    @Resource
    private OpenSearchClient openSearchClient;

    @PostConstruct
    public void initIndex() {
        //若索引不存在则创建
        createIndexIfNotExist();
        LogUtil.info("【初始化】OpenSearch索引完成");

        //获取已有的mapping
        OpenSearchMappingCache.syncFieldMappingFromOpenSearch(openSearchClient, SimpleProtoConfig.dataTable);

        //初始化基础对象字段的mapping
        boolean acknowledged = putDefaultMappingsIfMissing();
        if(acknowledged){
            //再次刷新mapping缓存
            OpenSearchMappingCache.syncFieldMappingFromOpenSearch(openSearchClient, SimpleProtoConfig.dataTable);
            LogUtil.info("刷新OpenSearchMapping缓存");
        }
        LogUtil.info("【初始化】OpenSearchMapping缓存完成");
    }

    /**
     * 若索引不存在则创建
     */
    private void createIndexIfNotExist(){
        try {
            // 索引是否存在,不存在则创建
            BooleanResponse response = openSearchClient.indices().exists(e -> e.index(SimpleProtoConfig.dataTable));
            if (!response.value()) {
                // 创建索引
                openSearchClient.indices().create(c -> c.index(SimpleProtoConfig.dataTable));
                LogUtil.info("初始化创建OpenSearch索引[{}]", SimpleProtoConfig.dataTable);
            }
        } catch (IOException e) {
            throw new SpSearchStorageException(e, "初始化OpenSearch索引[index=%s]失败", SimpleProtoConfig.dataTable);
        }
    }

    /**
     * 创建默认mapping若不存在
     * @return 是否创建
     */
    private boolean putDefaultMappingsIfMissing(){
        boolean needPutMapping = false;

        PutMappingRequest.Builder builder = new PutMappingRequest.Builder();
        builder.index(SimpleProtoConfig.dataTable);

        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpBaseObject.ID_FIELD)){
            builder.properties(ISpBaseObject.ID_FIELD, ob -> ob.long_(t -> t));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpBaseObject.TYPE_FIELD)){
            builder.properties(ISpBaseObject.TYPE_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpBaseObject.NAME_FIELD)){
            builder.properties(ISpBaseObject.NAME_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpBaseObject.CREATE_DATE_FIELD)){
            builder.properties(ISpBaseObject.CREATE_DATE_FIELD, ob -> ob.date(t -> t.format(SearchStorageClient.DEFAULT_DATE_FORMAT)));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpBaseObject.MODIFY_DATE_FIELD)){
            builder.properties(ISpBaseObject.MODIFY_DATE_FIELD, ob -> ob.date(t -> t.format(SearchStorageClient.DEFAULT_DATE_FORMAT)));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpBaseObject.IS_DELETE_FIELD)){
            builder.properties(ISpBaseObject.IS_DELETE_FIELD, ob -> ob.boolean_(t -> t));
            needPutMapping = true;
        }

        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpSysObject.TAG_FIELD)){
            builder.properties(ISpSysObject.TAG_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpSysObject.PATH_FIELD)){
            builder.properties(ISpSysObject.PATH_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpSysObject.PARENT_ID_FIELD)){
            builder.properties(ISpSysObject.PARENT_ID_FIELD, ob -> ob.long_(t -> t));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpSysObject.PARENT_PATH_FIELD)){
            builder.properties(ISpSysObject.PARENT_PATH_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }

        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpFile.FILE_TAG_FIELD)){
            builder.properties(ISpFile.FILE_TAG_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpFile.SUFFIX_FIELD)){
            builder.properties(ISpFile.SUFFIX_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpFile.FILE_SIZE_FIELD)){
            builder.properties(ISpFile.FILE_SIZE_FIELD, ob -> ob.long_(t -> t));
            needPutMapping = true;
        }
        if(!OpenSearchMappingCache.checkFieldExistInCache(ISpFile.FILE_STORAGE_KEY_FIELD)){
            builder.properties(ISpFile.FILE_STORAGE_KEY_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        //是否需要补充默认mapping执行
        boolean acknowledged = false;
        if(needPutMapping){
            try {
                acknowledged = openSearchClient.indices().putMapping(builder.build()).acknowledged();
                LogUtil.info("已在OpenSearch中补充缺少的默认mapping");
            } catch (IOException e) {
                throw new SpSearchStorageException(e, "在OpenSearch中补充默认mapping失败");
            }
        }
        return acknowledged;
    }
}
