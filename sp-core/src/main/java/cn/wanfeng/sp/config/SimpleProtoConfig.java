package cn.wanfeng.sp.config;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @date: 2024-10-27 13:40
 * @author: luozh.wanfeng
 * @description: 定义simpleproto项目所需的配置信息
 */
@Data
@Configuration
public class SimpleProtoConfig {

    public static String dataTable;
    public static String settingsTable;

    public static String postgresHost;
    public static String postgresPort;
    public static String postgresDatabase;
    public static String postgresScheme;

    public static String esUris;
    public static String esUsername;
    public static String esPassword;


    @Resource
    private ConfigurableEnvironment environment;

    public SimpleProtoConfig() {
    }

    @PostConstruct
    public void init() {
        dataTable = environment.getProperty("simpleproto.dataTable");
        settingsTable = environment.getProperty("simpleproto.settingsTable");

        postgresHost = environment.getProperty("simpleproto.postgresHost");
        postgresPort = environment.getProperty("simpleproto.postgresPort");
        postgresDatabase = environment.getProperty("spring.application.name");
        postgresScheme = environment.getProperty("spring.profiles.active");

        esUris = environment.getProperty("simpleproto.esUris");
        esUsername = environment.getProperty("simpleproto.esUsername");
        esPassword = environment.getProperty("simpleproto.esPassword");
    }

}
