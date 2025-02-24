package cn.wanfeng.sp.config.custom;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * SimpleStockConfig: stock模块配置.
 *
 * @date: 2025-02-10 16:41
 * @author: luozh.wanfeng
 */
@Data
@Configuration
public class SimpleStockConfig {

    /// from application.yml
    public static String apiSource;
    public static String apiToken;
    public static String apiResultFormat;

    // 接口返回结果json解析配置：json中为下划线，转为首字母小写驼峰
    public static ParserConfig responseParserConfig;

    @Resource
    private ConfigurableEnvironment environment;

    @PostConstruct
    public void init(){
        apiSource = environment.getProperty("spStock.apiSource");
        apiToken = environment.getProperty("spStock.apiToken");
        // 返回结果格式默认使用json
        apiResultFormat = "json";


        responseParserConfig = new ParserConfig();
        responseParserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }
}
