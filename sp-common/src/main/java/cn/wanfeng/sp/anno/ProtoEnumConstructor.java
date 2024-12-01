package cn.wanfeng.sp.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @date: 2024-11-28 21:08
 * @author: luozh.wanfeng
 * @description: 标记在枚举类的一个静态方法上，该静态方法根据值返回对应的枚举对象，赋值到simpleproto对象中
 * @since: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoEnumConstructor {
    String desc = "标记在枚举类的一个静态方法上，该静态方法根据值返回对应的枚举对象，赋值到simpleproto对象中";


}
