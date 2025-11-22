package cn.wanfeng.sp.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @date: 2024-10-20 14:38
 * @author: luozh.wanfeng
 * @description: 标注该类对应的type字段值
 * @since: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Type {
    String value();

    int number();
}
