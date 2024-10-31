package cn.wanfeng.sp.elastic;


import cn.wanfeng.sp.SimpleprotoApplicationTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;

/**
 * @date: 2024-10-31 22:48
 * @author: luozh.wanfeng
 */
public class ElasticSearchTest extends SimpleprotoApplicationTest {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testIndex() {

    }
}
