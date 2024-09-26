package cn.wanfeng.sp;

import cn.wanfeng.proto.util.staticutils.LogUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wanfeng
 */
@SpringBootApplication
@MapperScan(basePackages = {"cn.wanfeng.sp.**.mapper"})
public class SimpleprotoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleprotoApplication.class, args);
        LogUtils.info("SimpleProto Application Start Success (づ￣ 3￣)づ");
    }

}
