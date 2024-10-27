package cn.wanfeng.sp.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @date: 2024-10-27 13:40
 * @author: luozh.wanfeng
 * @description: 定义simpleproto项目所需的配置信息
 */
@Data
@ConfigurationProperties(prefix = "simpleproto")
@Component
public class SimpleProtoProperties {

    private String dataTable;

    private String settingsTable;
}
