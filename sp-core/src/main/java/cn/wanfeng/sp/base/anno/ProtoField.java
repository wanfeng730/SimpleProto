package cn.wanfeng.sp.base.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @date: 2024-06-24 09:43
 * @author: luozh
 * @description: 标注用户序列化的属性，在store或初始化时会根据index进行操作，name作为保存es中的字段
 * @since: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoField {
    int index();

    String name();
}
