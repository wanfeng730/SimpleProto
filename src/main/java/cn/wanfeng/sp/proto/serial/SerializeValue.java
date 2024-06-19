package cn.wanfeng.sp.proto.serial;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @date: 2024-02-16 12:46
 * @author: luozh
 * @since: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeValue {
    byte flag() default 0;
}
