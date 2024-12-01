package cn.wanfeng.sp;

import cn.wanfeng.sp.util.LogUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
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
public class SimpleprotoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleprotoApplication.class, args);
        LogUtils.info("SimpleProto Application Start Success (づ￣ 3￣)づ");
    }

}
