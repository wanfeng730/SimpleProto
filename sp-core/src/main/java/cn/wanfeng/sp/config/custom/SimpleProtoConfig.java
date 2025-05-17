package cn.wanfeng.sp.config.custom;


import cn.wanfeng.sp.api.domain.ISpSysObject;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Objects;

/**
 * @date: 2024-10-27 13:40
 * @author: luozh.wanfeng
 * @description: 定义simpleproto项目所需的配置信息
 */
@Data
@Configuration
public class SimpleProtoConfig {

    public static String appName;
    public static String appPort;
    public static String appContextPath;

    public static String currentScheme;
    public static String dataTable;
    public static String settingsTable;

    public static String opensearchHostScheme;
    public static String opensearchHost;
    public static Integer opensearchPort;
    public static String opensearchUsername;
    public static String opensearchPassword;

    public static String dataSourceDriver;
    public static String dataSourceUrl;
    public static String dataSourceUsername;
    public static String dataSourcePassword;

    public static String opensearchJdbcDriver;
    public static String opensearchJdbcUrl;
    public static String opensearchJdbcUseSSL;

    public static String redisHost;
    public static String redisPort;
    public static String redisPassword;
    public static String redisDatabase;

    public static final Long rootSysObjectId = -1L;
    public static String rootSysObjectPath = ISpSysObject.pathSeparator;

    /**
     * 创建用户默认密码
     */
    public static final String userDefaultPassword = "Wanfeng730";
    /**
     * 文件存储类型
     */
    public static final String FILE_STORAGE_TYPE_MINIO = "MinIO";

    public static String fileStorageType;
    public static String fileStorageEndPoint;
    public static String fileStorageAccessKey;
    public static String fileStorageSecretKey;
    public static String fileStorageBucket;

    public static String swaggerScanPackages;
    public static String swaggerTitle;
    public static String swaggerAuthor;
    public static String swaggerAuthorUrl;
    public static String swaggerAuthorEmail;
    public static String swaggerDescription;
    public static String swaggerVersion;

    @Resource
    private ConfigurableEnvironment environment;

    public SimpleProtoConfig() {
    }

    @PostConstruct
    public void init() {
        appName = environment.getProperty("spring.application.name");
        appPort = environment.getProperty("server.port");
        appContextPath = environment.getProperty("server.servlet.context-path");

        currentScheme = environment.getProperty("simpleproto.currentScheme");
        dataTable = environment.getProperty("simpleproto.dataTable");
        settingsTable = environment.getProperty("simpleproto.settingsTable");

        opensearchHostScheme = environment.getProperty("simpleproto.opensearchHostScheme");
        opensearchHost = environment.getProperty("simpleproto.opensearchHost");
        opensearchPort = Integer.valueOf(Objects.requireNonNull(environment.getProperty("simpleproto.opensearchPort")));
        opensearchUsername = environment.getProperty("simpleproto.opensearchUsername");
        opensearchPassword = environment.getProperty("simpleproto.opensearchPassword");

        dataSourceDriver = environment.getProperty("simpleproto.dataSourceDriver");
        dataSourceUrl = environment.getProperty("simpleproto.dataSourceUrl");
        dataSourceUsername = environment.getProperty("simpleproto.dataSourceUsername");
        dataSourcePassword = environment.getProperty("simpleproto.dataSourcePassword");

        opensearchJdbcDriver = environment.getProperty("simpleproto.opensearchJdbcDriver");
        opensearchJdbcUrl = environment.getProperty("simpleproto.opensearchJdbcUrl");
        opensearchJdbcUseSSL = environment.getProperty("simpleproto.opensearchJdbcUseSSL");

        redisHost = environment.getProperty("simpleproto.redisHost");
        redisPort = environment.getProperty("simpleproto.redisPort");
        redisPassword = environment.getProperty("simpleproto.redisPassword");
        redisDatabase = environment.getProperty("simpleproto.redisDatabase");

        fileStorageType = environment.getProperty("simpleproto.fileStorageType");
        fileStorageEndPoint = environment.getProperty("simpleproto.fileStorageEndPoint");
        fileStorageAccessKey = environment.getProperty("simpleproto.fileStorageAccessKey");
        fileStorageSecretKey = environment.getProperty("simpleproto.fileStorageSecretKey");
        fileStorageBucket = environment.getProperty("simpleproto.fileStorageBucket");

        swaggerScanPackages = environment.getProperty("simpleproto.swaggerScanPackages");
        swaggerTitle = environment.getProperty("simpleproto.swaggerTitle");
        swaggerAuthor = environment.getProperty("simpleproto.swaggerAuthor");
        swaggerAuthorUrl = environment.getProperty("simpleproto.swaggerAuthorUrl");
        swaggerAuthorEmail = environment.getProperty("simpleproto.swaggerAuthorEmail");
        swaggerDescription = environment.getProperty("simpleproto.swaggerDescription");
        swaggerVersion = environment.getProperty("simpleproto.swaggerVersion");

        System.out.println(" [SimpleProto初始化] simpleproto配置完成");
    }

}
