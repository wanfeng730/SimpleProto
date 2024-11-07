package cn.wanfeng.sp.config;


import cn.wanfeng.proto.exception.SpException;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.Resource;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

/**
 * @date: 2024-10-31 23:30
 * @author: luozh.wanfeng
 * @description: ElasticSearch配置
 * @since: 1.0
 */
@Configuration
public class ElasticSearchConfiguration {

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    @Bean
    public ElasticsearchClient getEsClientBean() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(SimpleProtoConfig.esUsername, SimpleProtoConfig.esPassword);
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        // es 8.x 版本官方部署的默认为https方式访问，需要配置ssl证书策略为允许所有
        SSLIOSessionStrategy sslioSessionStrategy = getAllAllowSSLStrategy();

        RestClient restClient = RestClient
                .builder(HttpHost.create(SimpleProtoConfig.esUris))
                .setHttpClientConfigCallback(hc -> hc
                        .setDefaultCredentialsProvider(credentialsProvider)
                        .disableAuthCaching()
                        .setSSLStrategy(sslioSessionStrategy))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    private static SSLIOSessionStrategy getAllAllowSSLStrategy() {
        SSLIOSessionStrategy sslioSessionStrategy;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial((x509Certificates, s) -> true).build();
            sslioSessionStrategy = new SSLIOSessionStrategy(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            throw new SpException("ElasticSearch初始化获取SSL允许所有策略失败", e);
        }
        return sslioSessionStrategy;
    }
}
