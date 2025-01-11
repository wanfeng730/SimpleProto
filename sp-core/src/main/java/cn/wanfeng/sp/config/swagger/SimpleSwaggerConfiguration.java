package cn.wanfeng.sp.config.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @date: 2025-01-11 16:46
 * @author: luozh.wanfeng
 * @description: 
 * @since: 1.1
 */
@Configuration
@EnableKnife4j
public class SimpleSwaggerConfiguration {

    // 此处为模块化配置，将API文档配置成几个模块，添加每个模块名，此次模块下所有API接口的统一前缀
    // 第一个模块
    @Bean
    public GroupedOpenApi PayApi() {
        return GroupedOpenApi.builder().group("教师文档API").pathsToMatch("/**").build();
    }


    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("swagger页面标题")
                        .description("描述")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs")
                );

    }


}
