package cn.wanfeng.sp.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Nonnull;

/**
 * ApplicationContextUtil: Spring上下文工具类.
 *
 * @date: 2025-07-02 21:54
 * @author: luozh.wanfeng
 */
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
