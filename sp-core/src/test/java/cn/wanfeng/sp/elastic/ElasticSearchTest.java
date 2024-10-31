package cn.wanfeng.sp.elastic;


import cn.wanfeng.sp.SimpleprotoApplicationTest;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @date: 2024-10-31 22:48
 * @author: luozh.wanfeng
 */
public class ElasticSearchTest extends SimpleprotoApplicationTest {

    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Test
    public void testIndex() throws IOException {
        String indexName = "unit_info";

        // 索引是否存在
        BooleanResponse books = elasticsearchClient.indices().exists(e -> e.index(indexName));
        System.out.println("索引是否存在：" + books.value());

        // 创建索引
        elasticsearchClient.indices().create(c -> c.index(indexName)
                .mappings(mappings -> mappings  // 映射
                        .properties("name", p -> p.text(t -> t.index(false)))
                        .properties("age", p -> p.long_(t -> t))
                )
        );

        // 删除索引
        elasticsearchClient.indices().delete(d -> d.index(indexName));
    }
}
