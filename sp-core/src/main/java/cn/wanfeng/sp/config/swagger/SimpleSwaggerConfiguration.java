package cn.wanfeng.sp.config.swagger;

import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.util.LogUtil;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @date: 2025-01-11 16:46
 * @author: luozh.wanfeng
 * @description: SwaggerUI配置
 * @since: 1.1
 */
@Configuration
@EnableKnife4j
public class SimpleSwaggerConfiguration {

    private static final String DEFAULT_SCAN_PACKAGE = "cn.wanfeng.sp.api.controller";

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    // 此处为模块化配置，将API文档配置成几个模块，添加每个模块名，此次模块下所有API接口的统一前缀
    // 第一个模块
    @Bean
    public GroupedOpenApi groupedOpenApi() {
        GroupedOpenApi openApi = GroupedOpenApi.builder()
                .group(SimpleProtoConfig.swaggerTitle)
                .packagesToScan(handleScanPackagesConfig(SimpleProtoConfig.swaggerScanPackages))
                .build();
        LogUtil.info("Swagger文档初始化成功，访问地址：http://localhost:{}{}/doc.html", SimpleProtoConfig.appPort, SimpleProtoConfig.appContextPath);
        return openApi;
    }


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(defaultSwaggerInfo());
    }

    private static final String packageSeparator = ",";

    private static Info defaultSwaggerInfo(){
        return new Info()
                .title(SimpleProtoConfig.swaggerTitle)
                .contact(new Contact()
                        .name(SimpleProtoConfig.swaggerAuthor)
                        .url(SimpleProtoConfig.swaggerAuthorUrl)
                        .email(SimpleProtoConfig.swaggerAuthorEmail))
                .description(SimpleProtoConfig.swaggerDescription)
                .version(SimpleProtoConfig.swaggerVersion)
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));
    }

    /**
     * 处理swagger扫描包路径
     * @param scanPackages 包路径（用逗号分割）
     * @return 包路径数组
     */
    private static String[] handleScanPackagesConfig(String scanPackages){
        if(StringUtils.isBlank(scanPackages)){
            return new String[0];
        }
        if(!scanPackages.contains(packageSeparator)){
            return new String[]{scanPackages};
        }
        String[] packages = scanPackages.split(packageSeparator);
        List<String> packageList = new ArrayList<>(Arrays.asList(packages));
        //去掉空的包名
        packageList.removeIf(StringUtils::isBlank);

        return packageList.toArray(new String[0]);
    }


}
