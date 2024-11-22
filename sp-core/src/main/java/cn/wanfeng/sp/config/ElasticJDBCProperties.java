package cn.wanfeng.sp.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @date: 2024-11-21 23:10
 * @author: luozh.wanfeng
 * @description: 连接ElasticSearch jdbc 配置
 * @since: 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.elastic")
public class ElasticJDBCProperties extends Properties {

    private String driver;
    private String url;
    private String user;
    private String password;
    private String useSSL;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.put("url", url);
    }

    public String getDriver() {
        return driver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUseSSL() {
        return useSSL;
    }

    public void setUseSSL(String useSSL) {
        this.useSSL = useSSL;
    }
}
