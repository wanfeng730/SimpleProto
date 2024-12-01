package cn.wanfeng.sp.util;

import cn.wanfeng.sp.anno.ProtoEnumConstructor;
import cn.wanfeng.sp.anno.ProtoEnumValue;
import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.exception.SimpleReflectException;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @date: 2024-10-20 20:50
 * @author: luozh.wanfeng
 * @description: SimpleProto反射工具类
 * @since: 1.0
 */
public class SimpleReflectUtils {

    /**
     * 获取类上标注ProtoField注解的属性
     */
    public static Field[] getProtoFieldAnnotationFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(ProtoField.class)).toArray(Field[]::new);
    }

    /**
     * 获取类上标注ProtoEnumConstructor的方法
     */
    public static Method getProtoEnumConstructorMethod(Class<?> clazz){
        List<Method> methodList = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(ProtoEnumConstructor.class)).toList();
        if(methodList.size() != 1){
            throw new SimpleReflectException("EnumClass[%s] must has Unique @ProtoEnumConstructor Method to build SpBaseObject", clazz.getName());
        }
        return methodList.getFirst();
    }

    /**
     * 获取类上标注ProtoEnumValue的方法
     */
    public static Method getProtoEnumValueMethod(Class<?> clazz){
        List<Method> methodList = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(ProtoEnumValue.class)).toList();
        if(methodList.size() != 1){
            throw new SimpleReflectException("EnumClass[%s] must has Unique @ProtoEnumValue Method to store SpBaseObject", clazz.getName());
        }
        return methodList.getFirst();
    }

    /**
     * 获取null对象的类型Class
     */
    public static Class<?> getNullObjectType(Object nullObject) {
        // 获取TypeUtils类的类加载器
        ClassLoader cl = TypeUtils.class.getClassLoader();

        // 使用匿名内部类来创建一个类型标记接口的实例
        Type type;
        try {
            type = new TypeReference<>(cl) {}.getType();
        } catch (ClassNotFoundException e) {
            throw new SimpleReflectException(e);
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type[] actualTypeArguments = paramType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                return (Class<?>) actualTypeArguments[0];
            }
        }
        return null;
    }

    // 内部类用于传递类加载器和获取泛型类型
    private static class TypeReference<T> {
        private final Class<T> type;

        protected TypeReference() {
            this.type = null; // 这里的type将会是null的类型
        }

        @SuppressWarnings("unchecked")
        public TypeReference(ClassLoader cl) throws ClassNotFoundException {
            this.type = (Class<T>) cl.loadClass(TypeUtils.class.getName());
        }

        public Type getType() {
            return this.type;
        }
    }

}
