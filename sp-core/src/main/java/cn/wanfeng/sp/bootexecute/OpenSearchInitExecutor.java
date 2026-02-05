package cn.wanfeng.sp.bootexecute;


import cn.wanfeng.sp.api.constant.OpenSearchPropertyType;
import cn.wanfeng.sp.api.domain.ISpBaseObject;
import cn.wanfeng.sp.api.domain.ISpFile;
import cn.wanfeng.sp.api.domain.ISpSysObject;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.exception.SpSearchStorageException;
import cn.wanfeng.sp.localcache.OpenSearchMappingCache;
import cn.wanfeng.sp.storage.search.SearchStorageClient;
import cn.wanfeng.sp.util.InputStreamUtils;
import cn.wanfeng.sp.util.LogUtil;
import cn.wanfeng.sp.util.ResourceFileUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch.indices.PutMappingRequest;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

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

    private static final Logger log = LogUtil.getSimpleProtoLogger();

    public static final String CUSTOM_MAPPINGS_RESOURCE_FILE_PATH = "domain_mappings/custom_mappings.json";

    @PostConstruct
    public void initIndex() {
        //若索引不存在则创建
        createIndexIfNotExist();
        log.info("初始化 >>> OpenSearch索引校验");

        //获取已有的mapping
        OpenSearchMappingCache.syncFieldMappingFromOpenSearch(openSearchClient, SimpleProtoConfig.dataTable);

        //初始化基础对象字段的mapping
        putDefaultMappingsIfMissing();
        log.info("初始化 >>> 加载基础对象mapping");

        // 初始化自定义字段的mapping
        putCustomMappingResource();
        log.info("初始化 >>> 加载自定义mapping");

        // 再次刷新mapping缓存
        OpenSearchMappingCache.syncFieldMappingFromOpenSearch(openSearchClient, SimpleProtoConfig.dataTable);
        log.info("初始化 >>> OpenSearchMapping缓存");
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
                log.info("初始化 >>> 创建OpenSearch索引[{}]", SimpleProtoConfig.dataTable);
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

        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpBaseObject.ID_FIELD, Property.Kind.Long)) {
            builder.properties(ISpBaseObject.ID_FIELD, ob -> ob.long_(t -> t));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpBaseObject.TYPE_FIELD, Property.Kind.Keyword)) {
            builder.properties(ISpBaseObject.TYPE_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpBaseObject.NAME_FIELD, Property.Kind.Keyword)) {
            builder.properties(ISpBaseObject.NAME_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpBaseObject.CREATE_DATE_FIELD, Property.Kind.Date)) {
            builder.properties(ISpBaseObject.CREATE_DATE_FIELD, ob -> ob.date(t -> t.format(SearchStorageClient.DEFAULT_DATE_FORMAT)));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpBaseObject.MODIFY_DATE_FIELD, Property.Kind.Date)) {
            builder.properties(ISpBaseObject.MODIFY_DATE_FIELD, ob -> ob.date(t -> t.format(SearchStorageClient.DEFAULT_DATE_FORMAT)));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpBaseObject.IS_DELETE_FIELD, Property.Kind.Boolean)) {
            builder.properties(ISpBaseObject.IS_DELETE_FIELD, ob -> ob.boolean_(t -> t));
            needPutMapping = true;
        }

        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpSysObject.TAG_FIELD, Property.Kind.Keyword)) {
            builder.properties(ISpSysObject.TAG_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpSysObject.PATH_FIELD, Property.Kind.Keyword)) {
            builder.properties(ISpSysObject.PATH_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpSysObject.PARENT_ID_FIELD, Property.Kind.Long)) {
            builder.properties(ISpSysObject.PARENT_ID_FIELD, ob -> ob.long_(t -> t));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpSysObject.PARENT_PATH_FIELD, Property.Kind.Keyword)) {
            builder.properties(ISpSysObject.PARENT_PATH_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }

        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpFile.FILE_TAG_FIELD, Property.Kind.Keyword)) {
            builder.properties(ISpFile.FILE_TAG_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpFile.SUFFIX_FIELD, Property.Kind.Keyword)) {
            builder.properties(ISpFile.SUFFIX_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpFile.FILE_SIZE_FIELD, Property.Kind.Long)) {
            builder.properties(ISpFile.FILE_SIZE_FIELD, ob -> ob.long_(t -> t));
            needPutMapping = true;
        }
        if (!OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, ISpFile.FILE_STORAGE_KEY_FIELD, Property.Kind.Keyword)) {
            builder.properties(ISpFile.FILE_STORAGE_KEY_FIELD, ob -> ob.keyword(t -> t));
            needPutMapping = true;
        }
        //是否需要补充默认mapping执行
        boolean acknowledged = false;
        if(needPutMapping){
            try {
                acknowledged = openSearchClient.indices().putMapping(builder.build()).acknowledged();
                log.info("已在OpenSearch中补充缺少的默认mapping");
            } catch (IOException e) {
                throw new SpSearchStorageException(e, "在OpenSearch中补充默认mapping失败");
            }
        }
        return acknowledged;
    }

    /**
     * 执行put自定义OpenSearch Mapping配置
     */
    private void putCustomMappingResource() {
        InputStream inputStream = ResourceFileUtils.getInputStream(CUSTOM_MAPPINGS_RESOURCE_FILE_PATH);
        if (Objects.isNull(inputStream)) {
            log.warn("未找到自定义OpenSearch的Mapping配置文件[{}]，不执行初始化", CUSTOM_MAPPINGS_RESOURCE_FILE_PATH);
            return;
        }

        PutMappingRequest.Builder builder = new PutMappingRequest.Builder();
        builder.index(SimpleProtoConfig.dataTable);

        String mappingJson = InputStreamUtils.getStringContent(inputStream);
        JSONObject properties = JSON.parseObject(mappingJson);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String fieldName = entry.getKey();
            if (!(entry.getValue() instanceof JSONObject fieldConfig)) {
                log.error("字段[{}]的mapping配置不是Object格式", fieldName);
                continue;
            }
            String fieldType = fieldConfig.getString("type");
            if (StringUtils.isBlank(fieldType)) {
                log.error("字段[{}]的mapping配置没有定义type", fieldName);
                continue;
            }

            if (OpenSearchPropertyType.type_keyword.equals(fieldType) && !OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, fieldName, Property.Kind.Keyword)) {
                builder.properties(fieldName, ob -> ob.keyword(t -> t));
                log.info("初始化 >>> OpenSearch自定义Mapping配置[{}]：{} -> {}", CUSTOM_MAPPINGS_RESOURCE_FILE_PATH, fieldName, fieldType);
            }
            if (OpenSearchPropertyType.type_integer.equals(fieldType) && !OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, fieldName, Property.Kind.Integer)) {
                builder.properties(fieldName, ob -> ob.integer(t -> t));
                log.info("初始化 >>> OpenSearch自定义Mapping配置[{}]：{} -> {}", CUSTOM_MAPPINGS_RESOURCE_FILE_PATH, fieldName, fieldType);
            }
            if (OpenSearchPropertyType.type_long.equals(fieldType) && !OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, fieldName, Property.Kind.Long)) {
                builder.properties(fieldName, ob -> ob.long_(t -> t));
                log.info("初始化 >>> OpenSearch自定义Mapping配置[{}]：{} -> {}", CUSTOM_MAPPINGS_RESOURCE_FILE_PATH, fieldName, fieldType);
            }
            if (OpenSearchPropertyType.type_boolean.equals(fieldType) && !OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, fieldName, Property.Kind.Boolean)) {
                builder.properties(fieldName, ob -> ob.boolean_(t -> t));
                log.info("初始化 >>> OpenSearch自定义Mapping配置[{}]：{} -> {}", CUSTOM_MAPPINGS_RESOURCE_FILE_PATH, fieldName, fieldType);
            }
            if (OpenSearchPropertyType.type_date.equals(fieldType) && !OpenSearchMappingCache.checkFieldExistInCache(SimpleProtoConfig.dataTable, fieldName, Property.Kind.Date)) {
                builder.properties(fieldName, ob -> ob.date(t -> t.format(SearchStorageClient.DEFAULT_DATE_FORMAT)));
                log.info("初始化 >>> OpenSearch自定义Mapping配置[{}]：{} -> {}", CUSTOM_MAPPINGS_RESOURCE_FILE_PATH, fieldName, fieldType);
            }
        }

        try {
            boolean acknowledged = openSearchClient.indices().putMapping(builder.build()).acknowledged();
            log.info("初始化 >>> 已加载自定义mapping文件[{}]", CUSTOM_MAPPINGS_RESOURCE_FILE_PATH);
        } catch (Exception e) {
            throw new SpException(e, "加载自定义mapping文件失败");
        }
    }
}
