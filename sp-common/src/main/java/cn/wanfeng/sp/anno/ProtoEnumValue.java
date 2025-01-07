package cn.wanfeng.sp.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @date: 2024-11-28 21:06
 * @author: luozh.wanfeng
 * @description: 标记在枚举类中获取值的方法上，该方法的返回值会作为simpleproto对象的值保存
 * @since: 1.0
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoEnumValue {

}
