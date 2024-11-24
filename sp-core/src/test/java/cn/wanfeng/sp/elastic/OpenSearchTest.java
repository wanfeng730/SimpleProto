package cn.wanfeng.sp.elastic;


import cn.wanfeng.sp.SimpleprotoApplicationTest;
import jakarta.annotation.Resource;
import org.opensearch.client.opensearch.OpenSearchClient;

/**
 * @date: 2024-11-24 18:08
 * @author: luozh.wanfeng
 * @description: OpenSearch功能测试
 * @since: 1.0
 */
public class OpenSearchTest extends SimpleprotoApplicationTest {

    @Resource
    private OpenSearchClient openSearchClient;

}
