package cn.wanfeng.sp.util;

import cn.wanfeng.sp.exception.SimpleConvertObjectException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @date: 2024-12-01 15:46
 * @author: luozh.wanfeng
 * @description: 类型转换工具类
 * @since: 1.0
 */
public class ObjectConvertUtils {

    /**
     * 源对象属性缓存
     */
    private static Map<String, Field> SOURCE_FIELD_NAME_MAP;

    /**
     * 转换对象类型，属性注入（类型和属性名一样才能转换）
     * @param object 源对象
     * @param targetClass 转换类型
     * @return 转换后的对象
     * @param <TargetType> 转换类型
     */
    public static <TargetType> TargetType convertObject(Object object, Class<TargetType> targetClass) {
        resetSourceFieldNameMap();
        try {
            Constructor<TargetType> constructor = targetClass.getConstructor();
            if(Objects.isNull(object)){
                return null;
            }
            TargetType targetObject = constructor.newInstance();

            Class<?> objectClass = object.getClass();
            List<Field> sourceFields = SimpleReflectUtils.getFieldsWithSuperClass(objectClass);
            addFieldsToSourceFieldNameMap(sourceFields);

            List<Field> targetFields = SimpleReflectUtils.getFieldsWithSuperClass(targetClass);
            if(CollectionUtils.isEmpty(targetFields)){
                return targetObject;
            }
            for (Field targetField : targetFields) {
                String targetFieldName = targetField.getName();
                if(!SOURCE_FIELD_NAME_MAP.containsKey(targetFieldName)){
                    continue;
                }
                Field sourceField = SOURCE_FIELD_NAME_MAP.get(targetFieldName);
                if(sourceField.getType() != targetField.getType()){
                    continue;
                }
                sourceField.setAccessible(true);
                targetField.setAccessible(true);

                Object value = sourceField.get(object);
                targetField.set(targetObject, value);
                LogUtils.debug("SourceField[Class={}, name={}, value={}] has Converted into ResultObject", sourceField.getType().getName(), sourceField.getName(), value);
            }
            return targetObject;
        } catch (Exception e) {
            throw new SimpleConvertObjectException(e);
        }
    }

    /**
     * 转换列表对象类型，属性注入（类型和属性名一样才能转换）
     * @param objectList 源对象列表
     * @param targetClass 目标类型
     * @return 转换后的列表
     * @param <TargetType> 目标类型
     */
    public static <TargetType> List<TargetType> convertList(List<Object> objectList, Class<TargetType> targetClass){
        if(Objects.isNull(objectList)){
            return null;
        }
        List<Object> validObjectList = objectList.stream().filter(Objects::nonNull).toList();
        if(CollectionUtils.isEmpty(validObjectList)){
            LogUtils.warn("objectList is Empty after Filter Null Elements，convertList() will Return Empty List");
            return new ArrayList<>();
        }
        resetSourceFieldNameMap();
        addFieldsToSourceFieldNameMap(List.of(validObjectList.getFirst().getClass().getFields()));

        List<TargetType> targetObjectList = new ArrayList<>();
        for (Object sourceObject : objectList) {
            TargetType targetObject = convertObjectNotSetMap(sourceObject, targetClass);
            targetObjectList.add(targetObject);
        }
        return targetObjectList;
    }



    private static <TargetType> TargetType convertObjectNotSetMap(Object object, Class<TargetType> targetClass) {
        try {
            Constructor<TargetType> constructor = targetClass.getConstructor();
            if(Objects.isNull(object)){
                return null;
            }
            TargetType targetObject = constructor.newInstance();

            Field[] targetFields = targetClass.getFields();
            if(ArrayUtils.isEmpty(targetFields)){
                return targetObject;
            }
            for (Field targetField : targetFields) {
                String targetFieldName = targetField.getName();
                if(!SOURCE_FIELD_NAME_MAP.containsKey(targetFieldName)){
                    continue;
                }
                Field sourceField = SOURCE_FIELD_NAME_MAP.get(targetFieldName);
                if(sourceField.getType() != targetField.getType()){
                    continue;
                }
                Object value = sourceField.get(object);
                targetField.setAccessible(true);
                targetField.set(targetObject, value);
                LogUtils.debug("属性[Class={}, name={}]已转换到结果中", targetField.getType().getName(), targetField.getName());
            }
            return targetObject;
        } catch (Exception e) {
            throw new SimpleConvertObjectException(e);
        }
    }

    private static void resetSourceFieldNameMap(){
        SOURCE_FIELD_NAME_MAP = new HashMap<>(8);
    }

    private static void addFieldsToSourceFieldNameMap(List<Field> fields){
        fields.forEach(field -> SOURCE_FIELD_NAME_MAP.put(field.getName(), field));
    }
}
