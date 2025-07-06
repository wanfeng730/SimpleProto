package cn.wanfeng.sp.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * ApplicationContextUtil: Spring上下文工具类.
 *
 * @date: 2025-07-02 21:54
 * @author: luozh.wanfeng
 *
 * 实现spring的ApplicationContextAware。
 * spring实例化所有bean后会将容器上下文ApplicationContext传递给ApplicationContextAware接口的实现类中
 * 实现ApplicationContextAware接口的bean可以通过接口方法获取到容器上下文
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <S> S getBean(Class<S> clazz) {
        return applicationContext.getBean(clazz);
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }
}
