package cn.wanfeng.sp.web.aspect;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * RequestInvokeAdvice: 请求调用日志打印 切面.
 *
 * @date: 2025-05-18 00:45
 * @author: luozh.wanfeng
 */
@Aspect
@Component
public class RequestInvokeLogAspect {

    /**
     * 定义切面位置
     */
    @Pointcut("execution(* cn.wanfeng.sp.web.controller.*.*(..))")
    public void invokeRequest() {

    }

    /**
     * 进入切面前调用的方法
     */
    @Before("invokeRequest()")
    public void logRequest(JoinPoint joinPoint) {
        HttpServletRequest request = getCurrentRequest();
        if (Objects.isNull(request)) {
            return;
        }
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String method = request.getMethod();

        String message = method + " " + uri;
        if (StringUtils.isNotBlank(queryString)) {
            message += "?" + queryString;
        }
        System.out.printf("[Request Invoke]: %s\n", message);
    }

    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }
}
