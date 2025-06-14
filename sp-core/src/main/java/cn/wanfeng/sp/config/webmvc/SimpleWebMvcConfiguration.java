package cn.wanfeng.sp.config.webmvc;


import cn.wanfeng.sp.util.LogUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

    // 一天
    private static final long MAX_AGE = 24 * 60 * 60;

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
        LogUtil.info("[SimpleProto WebMvc] 解决跨域问题");
    }

    /**
     * 配置静态资源映射 加载swagger的前端资源
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        LogUtil.info("[SimpleProto WebMvc] 加载Swagger前端资源");
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
}
