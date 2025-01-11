package cn.wanfeng.sp.context;

import cn.wanfeng.sp.util.LogUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @date: 2024-12-27 16:01
 * @author: luozh.wanfeng
 * @description: 实现spring的ApplicationContextAware。
 *      spring实例化所有bean后会将容器上下文ApplicationContext传递给ApplicationContextAware接口的实现类中
 *      实现ApplicationContextAware接口的bean可以通过接口方法获取到容器上下文
 * @since:
 */
@Component
public class SimpleApplicationContext implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T> T getBean(Class<T> clazz){
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        LogUtil.info("【初始化】SimpleApplicationContext 容器上下文初始化");
    }
}
