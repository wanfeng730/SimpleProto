package cn.wanfeng.proto.util;


import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @date: 2024-11-26 21:39
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public class ProtoReflectUtils {

    @SuppressWarnings("rawtypes")
    public static Class<?> getNullType(Object nullObj) {
        // 获取TypeUtils类的类加载器
        ClassLoader cl = TypeUtils.class.getClassLoader();

        // 使用匿名内部类来创建一个类型标记接口的实例
        Type type = null;
        try {
            type = new TypeReference<>(cl) {}.getType();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
