package cn.wanfeng.sp.util;

import cn.wanfeng.sp.anno.ProtoEnumConstructor;
import cn.wanfeng.sp.anno.ProtoEnumValue;
import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.exception.SimpleReflectException;
import cn.wanfeng.sp.exception.SpException;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        List<Field> allFields = getFieldsWithSuperClass(clazz);
        return allFields.stream().filter(field -> field.isAnnotationPresent(ProtoField.class)).toArray(Field[]::new);
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
     * 获取类及其所有父类的属性
     */
    public static List<Field> getFieldsWithSuperClass(Class<?> clazz){
        List<Field> fieldList = new ArrayList<>(Arrays.stream(clazz.getDeclaredFields()).toList());
        Class<?> superclass = clazz.getSuperclass();
        if(Objects.isNull(superclass)){
            return fieldList;
        }else {
            fieldList.addAll(getFieldsWithSuperClass(superclass));
        }
        return fieldList;
    }

    /**
     * 获取类中某个属性的setter方法
     * @param clazz 类
     * @param field 属性
     * @return Method
     */
    public static Method getSetterMethodByField(Class<?> clazz, Field field){
        if(Objects.isNull(clazz) || Objects.isNull(field)){
            return null;
        }
        try {
            // 获取属性的类型（先通过属性名拿到Field，再取类型）
            Class<?> fieldType = field.getType();
            String fieldName = field.getName();
            // 拼接set方法名：set + 首字母大写 + 剩余属性名
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            // 获取set方法（参数为属性类型，无返回值）
            Method setMethod = clazz.getMethod(methodName, fieldType);

            // 可选：校验是否为public的set方法
            if (Modifier.isPublic(setMethod.getModifiers())) {
                return setMethod;
            }
            LogUtil.warn("方法[{}]不为public修饰，不返回", setMethod.getName());
            return null;
        } catch (NoSuchMethodException e1){
            return null;
        }catch (Exception e) {
            throw new SpException(e, "获取类[%s]中属性[%s]的setter方法异常", clazz.getName(), field.getName());
        }
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
