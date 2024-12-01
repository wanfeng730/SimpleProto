package cn.wanfeng.proto.serial;

import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.proto.type.ProtoTypeConstants;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @date: 2024-02-08 20:16
 * @author: luozh
 * @since: 1.0
 */
public class DeserializeMethodContainer {

    @DeserializeValue(flag = ProtoTypeConstants.SMALL_INT_FLAG)
    public static Byte deserializeBytes2SmallInt(byte[] value) {
        if (value.length != ProtoTypeConstants.SMALL_INT_LENGTH) {
            throw new SpException("The length of small int value MUST BE " + ProtoTypeConstants.SMALL_INT_LENGTH + " but now is " + value.length);
        }
        return value[0];
    }

    @DeserializeValue(flag = ProtoTypeConstants.INT_FLAG)
    public static Integer deserializeBytes2Int(byte[] value) {
        if (value.length != ProtoTypeConstants.INT_LENGTH) {
            throw new SpException("The length of int value MUST BE " + ProtoTypeConstants.INT_LENGTH + " but now is " + value.length);
        }
        return DeserializeUtils.bytes2Int(value);
    }

    @DeserializeValue(flag = ProtoTypeConstants.LONG_FLAG)
    public static Long deserializeBytes2Long(byte[] value) {
        if (value.length != ProtoTypeConstants.LONG_LENGTH) {
            throw new SpException("The length of long value MUST BE " + ProtoTypeConstants.LONG_LENGTH + " but now is " + value.length);
        }
        return DeserializeUtils.bytes2Long(value);
    }

    @DeserializeValue(flag = ProtoTypeConstants.DOUBLE_FLAG)
    public static Double deserializeBytes2Double(byte[] value) {
        if (value.length != ProtoTypeConstants.DOUBLE_LENGTH) {
            throw new SpException("The length of double value MUST BE " + ProtoTypeConstants.DOUBLE_LENGTH + " but now is " + value.length);
        }
        return DeserializeUtils.bytes2Double(value);
    }

    @DeserializeValue(flag = ProtoTypeConstants.DATE_FLAG)
    public static Date deserializeBytes2Date(byte[] value) {
        if (value.length != ProtoTypeConstants.DATE_LENGTH) {
            throw new SpException("The length of date value MUST BE " + ProtoTypeConstants.DATE_LENGTH + " but now is " + value.length);
        }
        return DeserializeUtils.bytes2Date(value);
    }

    @DeserializeValue(flag = ProtoTypeConstants.BOOLEAN_FLAG)
    public static Boolean deserializeBytes2Boolean(byte[] value) {
        if (value.length != ProtoTypeConstants.BOOLEAN_LENGTH) {
            throw new SpException("The length of boolean value MUST BE " + ProtoTypeConstants.BOOLEAN_LENGTH + " but now is " + value.length);
        }
        return DeserializeUtils.bytes2Boolean(value);
    }

    @DeserializeValue(flag = ProtoTypeConstants.STRING_FLAG)
    public static String deserializeBytes2String(byte[] value) {
        if (value == null || value.length == 0) {
            return null;
        }
        return DeserializeUtils.bytes2String(value);
    }

    @DeserializeValue(flag = ProtoTypeConstants.TEXT_FLAG)
    public static String deserializeBytes2Text(byte[] value) {
        if (value == null || value.length == 0) {
            return null;
        }
        return DeserializeUtils.bytes2Text(value);
    }

    public static Map<Byte, Method> toFlagMethodMap() {
        HashMap<Byte, Method> methodMap = new HashMap<>();
        Arrays.stream(DeserializeMethodContainer.class.getMethods()).forEach(method -> {
            if (method.isAnnotationPresent(DeserializeValue.class)) {
                byte flag = method.getAnnotation(DeserializeValue.class).flag();
                methodMap.put(flag, method);
            }
        });
        return methodMap;
    }
}
