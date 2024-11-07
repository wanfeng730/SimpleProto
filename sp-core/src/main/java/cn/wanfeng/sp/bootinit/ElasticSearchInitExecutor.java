package cn.wanfeng.sp.bootinit;


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
        System.out.println("索引是否存在：" + books.value());
        if (!books.value()) {
            // 创建索引
            elasticsearchClient.indices().create(c -> c.index(SimpleProtoConfig.dataTable));
        }
    }
}
