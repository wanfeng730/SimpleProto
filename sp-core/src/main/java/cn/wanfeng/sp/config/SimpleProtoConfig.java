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

    public static String currentScheme;
    public static String dataTable;
    public static String settingsTable;

    public static String esUris;
    public static String esUsername;
    public static String esPassword;

    public static String dataSourceDriver;
    public static String dataSourceUrl;
    public static String dataSourceUsername;
    public static String dataSourcePassword;


    @Resource
    private ConfigurableEnvironment environment;

    public SimpleProtoConfig() {
    }

    @PostConstruct
    public void init() {
        currentScheme = environment.getProperty("simpleproto.currentScheme");
        dataTable = environment.getProperty("simpleproto.dataTable");
        settingsTable = environment.getProperty("simpleproto.settingsTable");

        esUris = environment.getProperty("simpleproto.esUris");
        esUsername = environment.getProperty("simpleproto.esUsername");
        esPassword = environment.getProperty("simpleproto.esPassword");

        dataSourceDriver = environment.getProperty("simpleproto.dataSourceDriver");
        dataSourceUrl = environment.getProperty("simpleproto.dataSourceUrl");
        dataSourceUsername = environment.getProperty("simpleproto.dataSourceUsername");
        dataSourcePassword = environment.getProperty("simpleproto.dataSourcePassword");
    }

}
