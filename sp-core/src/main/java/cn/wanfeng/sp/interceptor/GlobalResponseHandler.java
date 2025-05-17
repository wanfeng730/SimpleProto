package cn.wanfeng.sp.interceptor;


import cn.wanfeng.sp.model.ResponseEntity;
import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * GlobalResponseHandler: desc.
 *
 * @date: 2025-05-17 18:45
 * @author: luozh.wanfeng
 */
@RestControllerAdvice(basePackages = "cn.wanfeng.sp.api.controller")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否生效（返回的类型不是统一封装后的类）
     *
     * @param returnType    返回类型
     * @param converterType 转换类型
     * @return 是否需要处理调用beforeBodyWrite
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType() != ResponseEntity.class;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ResponseEntity responseEntity = ResponseEntity.success(body);

        // 返回类型不是 String：直接返回
        if (returnType.getParameterType() != String.class) {
            return responseEntity;
        }

        // 返回类型是 String：不能直接返回，需要进行额外处理
        // 将 Content-Type 设为 application/json ；因为如果直接返回String时，默认请求头中的 Content-Type = text/plain
        HttpHeaders headers = response.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 将 Result 转为 Json字符串 再返回
        return JSON.toJSONString(responseEntity);
    }
}
