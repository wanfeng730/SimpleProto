package cn.wanfeng.sp.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SimpleLogUtil: 增强的日志输出工具类.
 *
 * @date: 2025-03-18 00:34
 * @author: luozh.wanfeng
 */
public class PrettyLogUtil {

    private static final String BLANK = StringUtils.EMPTY;

    public static String prettyJson(Object data){
        if(Objects.isNull(data)){
            return BLANK;
        }
        return JSON.toJSONString(data, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }

    public static String prettyJson(String jsonString){
        if(StringUtils.isBlank(jsonString)){
            System.out.println("jsonString is Null or Blank");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        return prettyJson(jsonObject);
    }

    /**
     * 转换为漂亮json，忽略所有父类的属性，只输出自己的属性
     *
     * @param data 对象
     * @return 漂亮json
     */
    public static String prettyJsonIgnoreSuperClass(Object data) {
        return prettyJson(generateMapIgnoreSuperClass(data));
    }

    /**
     * 转换为json，忽略所有父类的属性，只输出自己的属性
     *
     * @param data 对象
     * @return json
     */
    public static String toJsonIgnoreSuperClass(Object data) {
        return JSON.toJSONString(generateMapIgnoreSuperClass(data));
    }

    private static Map<String, Object> generateMapIgnoreSuperClass(Object data) {
        if (Objects.isNull(data)) {
            return null;
        }
        Map<String, Object> declaredData = new HashMap<>();
        try {
            for (Field declaredField : data.getClass().getDeclaredFields()) {
                declaredField.setAccessible(true);
                if (!isSerializableField(declaredField)) {
                    continue;
                }
                Object value = declaredField.get(data);
                // 若为枚举类输出枚举的名字
                if (declaredField.getType().isEnum()) {
                    value = ((Enum<?>) value).name();
                }
                declaredData.put(declaredField.getName(), value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return declaredData;
    }

    /**
     * 是否为可序列化接口
     *
     * @param field 属性
     * @return 是否可序列化
     */
    private static boolean isSerializableField(Field field) {
        Class<?> fieldClass = field.getType();
        // 实现Serializable接口，可以转成json
        return Arrays.asList(fieldClass.getInterfaces()).contains(Serializable.class);
    }
}
