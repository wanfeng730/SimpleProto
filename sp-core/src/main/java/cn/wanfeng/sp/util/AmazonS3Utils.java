package cn.wanfeng.sp.util;


import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.SignerFactory;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * @date: 2024-11-09 00:38
 * @author: luozh.wanfeng
 * @description: 亚马逊S3存储工具类
 * @since: 1.0
 */
public class AmazonS3Utils {
    private static final int DEFAULT_PAGE_SIZE = 100000;

    /**
     * 获取aws客户端
     *
     * @return AmazonS3
     */
    public static AmazonS3 getClient(String endPoint, String accessKey, String secretKey) {
        ClientConfiguration configuration = new ClientConfiguration();
        configuration.withProtocol(Protocol.HTTP);
        configuration.setSignerOverride(SignerFactory.VERSION_FOUR_UNSIGNED_PAYLOAD_SIGNER);
        configuration.setMaxConnections(5000);// 设置最大连接数
        configuration.setConnectionTimeout(30000);// 设置连接超时30秒
        configuration.setSocketTimeout(120000);// 设置传输数据超时时间为120秒

        // 初始化S3客户端
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withClientConfiguration(configuration)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, null))
                .withPathStyleAccessEnabled(true).build();
    }

    /**
     * 查询指定桶，指定目录下的对象列表
     *
     * @param s3               客户端
     * @param bucketName       桶名
     * @param remoteFolderPath 目录路径
     * @return 对象信息列表
     */
    public static List<S3ObjectSummary> listFileByRemoteFolderPath(AmazonS3 s3, String bucketName, String remoteFolderPath) {
        List<S3ObjectSummary> objectSummaryList = new ArrayList<>();
        S3Objects.withPrefix(s3, bucketName, remoteFolderPath).withBatchSize(DEFAULT_PAGE_SIZE).forEach(objectSummaryList::add);
        return objectSummaryList;
    }

}
