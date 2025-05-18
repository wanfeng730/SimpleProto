package cn.wanfeng.sp.web.aspect;


import cn.wanfeng.sp.util.DateUtils;
import cn.wanfeng.sp.util.PrintColorUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
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
        Object requestBody = getRequestBodyObject(joinPoint);

        String message = method + " " + uri;
        if (StringUtils.isNotBlank(queryString)) {
            message += "?" + queryString;
        }
        System.out.printf("%s %s: %s\n", DateUtils.currentDateTimeMillis(), PrintColorUtil.cyan("[Request Invoke]"), message);
    }

    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }

    private static Object getRequestBodyObject(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法参数上的注解
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        if (ArrayUtils.isEmpty(parameterAnnotations)) {
            return null;
        }
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotationsOnOneParameter = parameterAnnotations[i];
            if (ArrayUtils.isEmpty(annotationsOnOneParameter)) {
                continue;
            }
            for (Annotation annotation : annotationsOnOneParameter) {
                if (annotation == null) {
                    continue;
                }
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (ClassUtils.isAssignable(annotationType, RequestBody.class)) {
                    return joinPoint.getArgs()[i];
                }
            }
        }
        return null;
    }
}
