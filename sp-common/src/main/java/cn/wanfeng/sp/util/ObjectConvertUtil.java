package cn.wanfeng.sp.util;

import cn.wanfeng.sp.exception.SimpleConvertObjectException;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @date: 2024-12-01 15:46
 * @author: luozh.wanfeng
 * @description: 类型转换工具类
 * @since: 1.0
 */
public class ObjectConvertUtil {

    /**
     * 转换对象类型，属性注入（类型和属性名一样才能转换）
     * @param object 源对象
     * @param targetClass 转换类型
     * @return 转换后的对象
     * @param <TargetType> 转换类型
     */
    public static <TargetType> TargetType convertObject(Object object, Class<TargetType> targetClass) {
        Map<String, Field> sourceFieldNameMap = getObjectFieldNameMap(object);
        return convertObjectFastly(object, targetClass, sourceFieldNameMap);
    }

    /**
     * 转换列表对象类型，属性注入（类型和属性名一样才能转换）
     * @param objectList 源对象列表
     * @param targetClass 目标类型
     * @return 转换后的列表
     * @param <TargetType> 目标类型
     */
    public static <TargetType> List<TargetType> convertList(List<?> objectList, Class<TargetType> targetClass){
        if(CollectionUtils.isEmpty(objectList)){
            return new ArrayList<>();
        }
        List<?> validObjectList = objectList.stream().filter(Objects::nonNull).toList();
        if(CollectionUtils.isEmpty(validObjectList)){
            LogUtil.error("objectList is Empty after Filter Null Elements，convertList() will Return Empty List");
            return new ArrayList<>();
        }
        // 获取列表元素Class的属性map
        Map<String, Field> sourceFieldNameMap = getObjectFieldNameMap(validObjectList.getFirst());

        List<TargetType> targetObjectList = new ArrayList<>();
        for (Object sourceObject : objectList) {
            TargetType targetObject = convertObjectFastly(sourceObject, targetClass, sourceFieldNameMap);
            targetObjectList.add(targetObject);
        }
        return targetObjectList;
    }

    /**
     * 转换对象类型，属性注入（使用提前定义好的字段映射进行转换，在进行集合转换时提高效率
     * @param object 源对象
     * @param targetClass 转换类型
     * @param sourceFieldNameMap 提前定义的源对象类中的属性映射  属性名称 -> 反射Field对象
     * @return 转换对象
     * @param <TargetType> 转换类型
     */
    private static <TargetType> TargetType convertObjectFastly(Object object, Class<TargetType> targetClass, Map<String, Field> sourceFieldNameMap) {
        try {
            Constructor<TargetType> constructor = targetClass.getConstructor();
            if(Objects.isNull(object)){
                return null;
            }
            TargetType targetObject = constructor.newInstance();

            List<Field> targetFields = SimpleReflectUtils.getFieldsWithSuperClass(targetClass);
            if(CollectionUtils.isEmpty(targetFields)){
                return targetObject;
            }
            for (Field targetField : targetFields) {
                String targetFieldName = targetField.getName();
                if(!sourceFieldNameMap.containsKey(targetFieldName)){
                    continue;
                }
                Field sourceField = sourceFieldNameMap.get(targetFieldName);
                if(sourceField.getType() != targetField.getType()){
                    continue;
                }
                sourceField.setAccessible(true);
                targetField.setAccessible(true);

                Object value = sourceField.get(object);
                targetField.set(targetObject, value);
            }
            return targetObject;
        } catch (Exception e) {
            throw new SimpleConvertObjectException(e);
        }
    }

    /**
     * 将一个对象的属性生成属性名的map
     * @param object 对象
     * @return map
     */
    private static Map<String, Field> getObjectFieldNameMap(Object object){
        Class<?> objectClass = object.getClass();
        List<Field> sourceFields = SimpleReflectUtils.getFieldsWithSuperClass(objectClass);
        if(CollectionUtils.isEmpty(sourceFields)){
            return new HashMap<>(2);
        }
        return sourceFields.stream().collect(Collectors.toMap(Field::getName, field -> field));
    }
}
