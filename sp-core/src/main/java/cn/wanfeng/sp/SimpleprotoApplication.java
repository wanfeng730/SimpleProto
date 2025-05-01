package cn.wanfeng.sp;

import cn.wanfeng.sp.config.swagger.SimpleSwaggerConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wanfeng
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@MapperScan(basePackages = {"cn.wanfeng.sp.**.mapper"})
@EnableRetry
@EnableAsync
@EnableDiscoveryClient
public class SimpleprotoApplication {

    private static final String VERSION = "1.1";

    public static void main(String[] args) {
        SpringApplication.run(SimpleprotoApplication.class, args);
        System.out.println();
        System.out.println("SimpleProto Application Start Success (づ￣ 3￣)づ");
        System.out.println("* Version: " + VERSION);
        System.out.println("* Swagger Browse Url: " + SimpleSwaggerConfiguration.swaggerBrowseUrl);
        System.out.println();
    }

}
