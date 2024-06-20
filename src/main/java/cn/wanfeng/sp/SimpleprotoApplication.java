package cn.wanfeng.sp;

import cn.wanfeng.sp.util.LogUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleprotoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleprotoApplication.class, args);
        LogUtils.info("SimpleProto Application Start Success (づ￣ 3￣)づ");
    }

}
