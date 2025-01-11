package cn.wanfeng.sp.config.swagger;

import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.Resource;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @date: 2025-01-11 16:46
 * @author: luozh.wanfeng
 * @description: SwaggerUI配置
 * @since: 1.1
 */
@Configuration
@EnableKnife4j
public class SimpleSwaggerConfiguration {

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    // 此处为模块化配置，将API文档配置成几个模块，添加每个模块名，此次模块下所有API接口的统一前缀
    // 第一个模块
    @Bean
    public GroupedOpenApi PayApi() {
        return GroupedOpenApi.builder().group("SimpleProto-API 接口文档").pathsToMatch("/**").build();
    }


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(defaultSwaggerInfo());
    }

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



}
