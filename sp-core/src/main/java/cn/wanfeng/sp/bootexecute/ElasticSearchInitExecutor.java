package cn.wanfeng.sp.bootexecute;


import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.config.SimpleProtoConfig;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @date: 2024-11-07 21:20
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Component
public class ElasticSearchInitExecutor {

    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    @PostConstruct
    public void initIndex() throws IOException {
        // 索引是否存在,不存在则创建
        BooleanResponse books = elasticsearchClient.indices().exists(e -> e.index(SimpleProtoConfig.dataTable));
        if (!books.value()) {
            // 创建索引
            elasticsearchClient.indices().create(c -> c.index(SimpleProtoConfig.dataTable));
            LogUtils.info("初始化创建ElasticSearch索引[{}]", SimpleProtoConfig.dataTable);
        }
    }
}
