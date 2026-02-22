package cn.wanfeng.sp.config.custom;


import cn.wanfeng.sp.api.domain.ISpSysObject;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
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
    public static String[] corsAllowedOriginPatterns;

    public static String currentScheme;
    public static String dataTable;
    public static String settingsTable;

    public static String opensearchHostScheme;
    public static String opensearchHost;
    public static Integer opensearchPort;
    public static String opensearchUsername;
    public static String opensearchPassword;
    public static Integer opensearchConnectTimeout = 60000;
    public static Integer opensearchSocketTimeout = 30000;
    public static long opensearchRequestTimeout;
    public static String opensearchDomainClassBasePackage;
    public static String[] opensearchDomainClassBasePackages;

    public static String dataSourceDriver;
    public static String dataSourceUrl;
    public static String dataSourceUsername;
    public static String dataSourcePassword;
    public static Integer dataSourceMaximumPoolSize;
    public static Integer dataSourceMinimumIdle;
    public static Long dataSourceConnectionTimeout;
    public static Long dataSourceIdleTimeout;
    public static Long dataSourceMaxLifetime;
    public static Long dataSourceKeepAliveTime;
    public static String dataSourceConnectionTestQuery;
    public static Long dataSourceValidationTimeout;
    public static Long dataSourceLeakDetectionThreshold;

    public static String opensearchJdbcDriver;
    public static String opensearchJdbcUrl;
    public static String opensearchJdbcUseSSL;

    public static String redisHost;
    public static String redisPort;
    public static String redisUsername;
    public static String redisPassword;
    public static Integer redisDatabase;
    public static Integer redisPoolMaxSize = 8;
    public static Integer redisPoolMinIdle = 2;
    public static Integer redisPoolConnectTimeout = 5000;
    public static Integer redisPoolIdleConnectionTimeout = 10000;
    public static Integer redisPoolPingConnectionInterval = 30000;

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

    private static final Logger log = LogUtil.getSimpleProtoLogger();

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
        opensearchPort = environment.getProperty("simpleproto.opensearchPort", Integer.class);
        opensearchUsername = environment.getProperty("simpleproto.opensearchUsername");
        opensearchPassword = environment.getProperty("simpleproto.opensearchPassword");
        opensearchConnectTimeout = environment.getProperty("simpleproto.opensearchConnectTimeout", Integer.class);
        opensearchSocketTimeout = environment.getProperty("simpleproto.opensearchSocketTimeout", Integer.class);
        // opensearch请求超时时间 < 客户端传输时间SocketTimeout
        opensearchRequestTimeout = Objects.isNull(opensearchSocketTimeout) ? 30000L : opensearchSocketTimeout - 5000;

        dataSourceDriver = environment.getProperty("simpleproto.dataSourceDriver");
        dataSourceUrl = environment.getProperty("simpleproto.dataSourceUrl");
        dataSourceUsername = environment.getProperty("simpleproto.dataSourceUsername");
        dataSourcePassword = environment.getProperty("simpleproto.dataSourcePassword");
        dataSourceMaximumPoolSize = environment.getProperty("simpleproto.dataSourceMaximumPoolSize", Integer.class);
        dataSourceMinimumIdle = environment.getProperty("simpleproto.dataSourceMinimumIdle", Integer.class);
        dataSourceConnectionTimeout = environment.getProperty("simpleproto.dataSourceConnectionTimeout", Long.class);
        dataSourceIdleTimeout = environment.getProperty("simpleproto.dataSourceIdleTimeout", Long.class);
        dataSourceMaxLifetime = environment.getProperty("simpleproto.dataSourceMaxLifetime", Long.class);
        dataSourceKeepAliveTime = environment.getProperty("simpleproto.dataSourceKeepAliveTime", Long.class);
        dataSourceConnectionTestQuery = environment.getProperty("simpleproto.dataSourceConnectionTestQuery", String.class);
        dataSourceValidationTimeout = environment.getProperty("simpleproto.dataSourceValidationTimeout", Long.class);
        dataSourceLeakDetectionThreshold = environment.getProperty("simpleproto.dataSourceLeakDetectionThreshold", Long.class);

        opensearchJdbcDriver = environment.getProperty("simpleproto.opensearchJdbcDriver");
        opensearchJdbcUrl = environment.getProperty("simpleproto.opensearchJdbcUrl");
        opensearchJdbcUseSSL = environment.getProperty("simpleproto.opensearchJdbcUseSSL");

        redisHost = environment.getProperty("simpleproto.redisHost");
        redisPort = environment.getProperty("simpleproto.redisPort");
        redisUsername = environment.getProperty("simpleproto.redisUsername");
        redisPassword = environment.getProperty("simpleproto.redisPassword");
        redisDatabase = environment.getProperty("simpleproto.redisDatabase", Integer.class);
        redisPoolMaxSize = environment.getProperty("simpleproto.redisPoolMaxSize", Integer.class);
        redisPoolMinIdle = environment.getProperty("simpleproto.redisPoolMinIdle", Integer.class);
        redisPoolConnectTimeout = environment.getProperty("simpleproto.redisPoolConnectTimeout", Integer.class);
        redisPoolIdleConnectionTimeout = environment.getProperty("simpleproto.redisPoolIdleConnectionTimeout", Integer.class);
        redisPoolPingConnectionInterval = environment.getProperty("simpleproto.redisPoolPingConnectionInterval", Integer.class);

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

        String patternsStr = environment.getProperty("simpleproto.corsAllowedOriginPatterns");
        boolean isManyPatterns = StringUtils.isNotBlank(patternsStr) && patternsStr.contains(",");
        corsAllowedOriginPatterns = isManyPatterns ? patternsStr.replace(" ", "").split(",") : new String[]{};

        log.info("初始化 >>> SimpleProtoConfig配置");
    }

}
