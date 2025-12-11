package cn.wanfeng.sp.config.opensearch;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.util.LogUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadFactory;

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

    private static final Logger log = LogUtil.getSimpleProtoLogger();

    @Bean
    public OpenSearchClient openSearchClient() {
        final HttpHost host = new HttpHost(SimpleProtoConfig.opensearchHost, SimpleProtoConfig.opensearchPort, SimpleProtoConfig.opensearchHostScheme);
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //Only for demo purposes. Don't specify your credentials in code.
        credentialsProvider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials(SimpleProtoConfig.opensearchUsername, SimpleProtoConfig.opensearchPassword));

        //Initialize the client
        final RestClient restClient = RestClient.builder(host)
                .setHttpClientConfigCallback(httpClientBuilder -> initOpenSearchHttpClient(httpClientBuilder, credentialsProvider))
                .build();

        // 保存opensearch的数据时序列化所有字段（包括 null）
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        final OpenSearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper(objectMapper));

        OpenSearchClient openSearchClient = new OpenSearchClient(transport);
        log.info("初始化 OpenSearch客户端完成");
        return openSearchClient;
    }

    private static HttpAsyncClientBuilder initOpenSearchHttpClient(HttpAsyncClientBuilder builder, BasicCredentialsProvider credentialsProvider) {
        // 登录认证信息
        builder.setDefaultCredentialsProvider(credentialsProvider);

        RequestConfig requestConfig = buildOpenSearchRequestConfig();
        builder.setDefaultRequestConfig(requestConfig);
        // 最大总连接数
        builder.setMaxConnTotal(100);
        // 每个路由（目标主机）的最大连接数
        builder.setMaxConnPerRoute(20);
        // 空闲连接存活时间（毫秒）
        builder.setKeepAliveStrategy((response, context) -> 300000);
        // IO线程
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("OpenSearch-Http-Client-")
                .setDaemon(true)
                .build();
        builder.setThreadFactory(threadFactory);
        // 连接复用策略
        builder.setConnectionReuseStrategy(new DefaultConnectionReuseStrategy());
        return builder;
    }

    /**
     * 构造http请求基本配置
     * 1. setConnectTimeout：连接建立超时（毫秒）
     * 2. setSocketTimeout：数据传输超时（毫秒）
     * 3. setConnectionRequestTimeout： 从连接池获取连接的超时（毫秒）
     *
     * @return RequestConfig
     */
    private static RequestConfig buildOpenSearchRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(SimpleProtoConfig.opensearchConnectTimeout)
                .setSocketTimeout(SimpleProtoConfig.opensearchSocketTimeout)
                .setConnectionRequestTimeout(5000)
                .build();

    }
}
