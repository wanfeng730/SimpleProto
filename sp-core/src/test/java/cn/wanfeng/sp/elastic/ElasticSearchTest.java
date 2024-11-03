package cn.wanfeng.sp.elastic;


import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.base.domain.SpBaseObject;
import cn.wanfeng.sp.base.object.SpSettingsSearchDO;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        LogUtils.info("ElasticSearch索引功能测试完成");
    }


    @Test
    public void testDocument() throws IOException, InterruptedException {
        String indexName = "test_settings_documents";
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", SpBaseObject.BASE_OBJECT_ID_INCREASE_NAME);
        dataMap.put("increase_long", 888);
        dataMap.put("increase_string", "AAABBBCCC");

        // 索引是否存在,不存在则创建
        BooleanResponse books = elasticsearchClient.indices().exists(e -> e.index(indexName));
        System.out.println("索引是否存在：" + books.value());
        if (!books.value()) {
            // 创建索引
            elasticsearchClient.indices().create(c -> c.index(indexName)
                    .mappings(mappings -> mappings  // 映射
                            .properties("name", p -> p.keyword(t -> t.index(true)))
                            .properties("increase_long", p -> p.long_(t -> t))
                            .properties("increase_string", p -> p.keyword(t -> t))
                    )
            );
        }

        // 新建数据
        SpSettingsSearchDO spSettingsSearchDO = new SpSettingsSearchDO(SpBaseObject.BASE_OBJECT_ID_INCREASE_NAME, 3L, null);
        IndexRequest<Map<String, Object>> indexRequest = IndexRequest.of(b -> b
                .index(indexName)
                .id((String) dataMap.get("name"))
                .document(dataMap)
        );
        elasticsearchClient.index(indexRequest);

        // 修改数据
        dataMap.put("increase_string", "XYZ");
        UpdateRequest<Map<String, Object>, Map<String, Object>> updateRequest = UpdateRequest.of(req -> req
                .index(indexName)
                .id((String) dataMap.get("name"))
                .doc(dataMap)
        );
        elasticsearchClient.update(updateRequest, Map.class);

        // 查看数据
        SearchResponse<Map> searchResponse = elasticsearchClient.search(s -> s.index(indexName).query(q -> q
                        .term(t -> t.field("name").value(((String) dataMap.get("name")))
                        )
                ), Map.class
        );
        TotalHits totalHits = searchResponse.hits().total();
        List<Hit<Map>> hitList = searchResponse.hits().hits();
        List<Map> resultSettingList = new ArrayList<>();
        for (Hit<Map> hit : hitList) {
            resultSettingList.add(hit.source());
        }
        Assertions.assertNotNull(totalHits);
        Assertions.assertEquals(1, totalHits.value());
        Assertions.assertTrue(CollectionUtils.isNotEmpty(resultSettingList));

        // 删除数据
        DeleteRequest deleteRequest = DeleteRequest.of(req -> req
                .index(indexName)
                .id(spSettingsSearchDO.getName())
        );
        elasticsearchClient.delete(deleteRequest);

        // es太快，还没删完就查询仍然会拿到数据，先睡吧~
        Thread.sleep(3000);

        // 查看数据
        searchResponse = elasticsearchClient.search(s -> s.index(indexName).query(q -> q
                        .term(t -> t.field("name").value(((String) dataMap.get("name")))
                        )
                ), Map.class
        );
        totalHits = searchResponse.hits().total();
        hitList = searchResponse.hits().hits();
        resultSettingList = new ArrayList<>();
        for (Hit<Map> hit : hitList) {
            resultSettingList.add(hit.source());
        }
        Assertions.assertNotNull(totalHits);
        Assertions.assertEquals(0, totalHits.value());
        Assertions.assertTrue(CollectionUtils.isEmpty(resultSettingList));

        LogUtils.info("ElasticSearch文档功能测试完成");
    }

}
