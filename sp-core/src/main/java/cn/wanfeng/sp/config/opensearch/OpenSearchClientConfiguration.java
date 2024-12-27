package cn.wanfeng.sp.config.opensearch;

import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.Resource;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @date: 2024-10-31 23:30
 * @author: luozh.wanfeng
 * @description: ElasticSearch配置
 * @since: 1.0
 */
@Configuration
public class OpenSearchClientConfiguration {

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    @Bean
    public OpenSearchClient openSearchClient() {
        final HttpHost host = new HttpHost(SimpleProtoConfig.opensearchHost, SimpleProtoConfig.opensearchPort, SimpleProtoConfig.opensearchHostScheme);
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //Only for demo purposes. Don't specify your credentials in code.
        credentialsProvider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials(SimpleProtoConfig.opensearchUsername, SimpleProtoConfig.opensearchPassword));

        //Initialize the client
        final RestClient restClient = RestClient.builder(host).setHttpClientConfigCallback(httpClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
        ).build();

        final OpenSearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        OpenSearchClient openSearchClient = new OpenSearchClient(transport);
        LogUtil.info("【初始化】OpenSearch客户端完成");
        return openSearchClient;
    }
}
