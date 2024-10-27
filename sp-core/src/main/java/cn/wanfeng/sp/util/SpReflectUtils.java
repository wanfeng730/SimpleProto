package cn.wanfeng.sp.util;


import cn.wanfeng.sp.anno.ProtoField;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @date: 2024-10-20 20:50
 * @author: luozh.wanfeng
 * @description: sp模块 反射工具类
 * @since:
 */
public class SpReflectUtils {

    public static Field[] getProtoFieldAnnotationFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(ProtoField.class)).toArray(Field[]::new);
    }
}
