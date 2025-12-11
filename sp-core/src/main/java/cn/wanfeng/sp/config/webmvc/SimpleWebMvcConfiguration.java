package cn.wanfeng.sp.config.webmvc;


import cn.wanfeng.sp.util.LogUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * SimpleWebMvcConfiguration: desc.
 *
 * @date: 2025-06-02 13:29
 * @author: luozh.wanfeng
 */
@Configuration
public class SimpleWebMvcConfiguration extends WebMvcConfigurationSupport {

    private static final Logger log = LogUtil.getSimpleProtoLogger();

    // 一天
    private static final long MAX_AGE = 24 * 60 * 60;

    @Bean
    public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new OrderedHiddenHttpMethodFilter();
    }

    /**
     * 解决跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")         // 对当前路径下的所有请求都应用当前的跨域配置
                .allowedOriginPatterns("*")         // 替换这个
                .allowedMethods("*")                // 允许的请求方法，可以指定具体的，如："GET"、"POST"、"PUT"、"DELETE"
                .allowedHeaders("*")                // 允许的请求头类型，可以指定具体的，如："Content-Type", "Authorization
                .allowCredentials(true)             // 允许凭证
                .maxAge(MAX_AGE);                   // 设置请求最大有效时长，在这个时长内，重复的请求就不会发送预检请求
        log.info("初始化 CORS配置解决跨域问题");
    }

    /**
     * 配置静态资源映射 加载swagger的前端资源
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        log.info("初始化 Resources资源文件路径映射配置 doc.html -> classpath:/META-INF/resources/doc.html");

        registry.addResourceHandler("favicon.ico").addResourceLocations("classpath:/static/");
        log.info("初始化 Resources资源文件路径映射配置 favicon.ico -> classpath:/static/favicon.ico");

        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        log.info("初始化 Resources资源文件路径映射配置 /webjars/** -> classpath:/META-INF/resources/webjars/**");

        super.addResourceHandlers(registry);
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = converter.getObjectMapper();
        // 生成JSON时,将所有Long转换成String
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        // 时间格式化
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 设置格式化内容
        converter.setObjectMapper(objectMapper);

        // 转换器放在第二个位置，保证swagger正常访问，时间格式可以正常转换
        converters.add(1, converter);
    }


    /**
     * 配置校验消息源（国际化/自定义提示）
     */
    @Bean
    public MessageSource validationMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }

    /**
     * 配置 Validator 核心Bean（整合Hibernate Validator + 自定义消息源）
     */
    @Bean
    public Validator validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(validationMessageSource());
        // validator.getValidationPropertyMap().put(
        //         "hibernate.validator.fail_fast", "true"
        // );
        // 可选：指定 Hibernate Validator 提供商
        validator.setProviderClass(HibernateValidator.class);

        return validator;
    }

    /**
     * 重写 WebMvcConfigurationSupport 的 getValidator 方法
     * 让 Spring MVC 接管此 Validator 进行参数校验
     */
    @Override
    protected Validator getValidator() {
        return validator();
    }
}
