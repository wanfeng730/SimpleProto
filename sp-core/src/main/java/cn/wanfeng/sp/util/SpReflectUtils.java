package cn.wanfeng.sp.util;


import cn.wanfeng.proto.exception.SpException;
import cn.wanfeng.sp.anno.ProtoEnumConstructor;
import cn.wanfeng.sp.anno.ProtoEnumValue;
import cn.wanfeng.sp.anno.ProtoField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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

    public static Method getProtoEnumConstructorMethod(Class<?> clazz){
        List<Method> methodList = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(ProtoEnumConstructor.class)).toList();
        if(methodList.size() != 1){
            throw new SpException("EnumClass[%s] must has Unique @ProtoEnumConstructor Method to build SpBaseObject", clazz.getName());
        }
        return methodList.getFirst();
    }

    public static Method getProtoEnumValueMethod(Class<?> clazz){
        List<Method> methodList = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(ProtoEnumValue.class)).toList();
        if(methodList.size() != 1){
            throw new SpException("EnumClass[%s] must has Unique @ProtoEnumValue Method to store SpBaseObject", clazz.getName());
        }
        return methodList.getFirst();
    }

}
