package cn.wanfeng.sp.proto.serial;

import cn.wanfeng.sp.proto.exception.ProtoException;
import cn.wanfeng.sp.proto.type.TypeConstants;

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

    @DeserializeValue(flag = TypeConstants.SMALL_INT_FLAG)
    public static Byte deserializeBytes2SmallInt(byte[] value) {
        if (value.length != TypeConstants.SMALL_INT_LENGTH) {
            throw new ProtoException("The length of small int value MUST BE " + TypeConstants.SMALL_INT_LENGTH + " but now is " + value.length);
        }
        return value[0];
    }

    @DeserializeValue(flag = TypeConstants.INT_FLAG)
    public static Integer deserializeBytes2Int(byte[] value) {
        if (value.length != TypeConstants.INT_LENGTH) {
            throw new ProtoException("The length of int value MUST BE " + TypeConstants.INT_LENGTH + " but now is " + value.length);
        }
        return DeserializeUtils.bytes2Int(value);
    }

    @DeserializeValue(flag = TypeConstants.LONG_FLAG)
    public static Long deserializeBytes2Long(byte[] value) {
        if (value.length != TypeConstants.LONG_LENGTH) {
            throw new ProtoException("The length of long value MUST BE " + TypeConstants.LONG_LENGTH + " but now is " + value.length);
        }
        return DeserializeUtils.bytes2Long(value);
    }

    @DeserializeValue(flag = TypeConstants.DOUBLE_FLAG)
    public static Double deserializeBytes2Double(byte[] value) {
        if (value.length != TypeConstants.DOUBLE_LENGTH) {
            throw new ProtoException("The length of double value MUST BE " + TypeConstants.DOUBLE_LENGTH + " but now is " + value.length);
        }
        return DeserializeUtils.bytes2Double(value);
    }

    @DeserializeValue(flag = TypeConstants.DATE_FLAG)
    public static Date deserializeBytes2Date(byte[] value) {
        if (value.length != TypeConstants.DATE_LENGTH) {
            throw new ProtoException("The length of date value MUST BE " + TypeConstants.DATE_LENGTH + " but now is " + value.length);
        }
        return DeserializeUtils.bytes2Date(value);
    }

    @DeserializeValue(flag = TypeConstants.BOOLEAN_FLAG)
    public static Boolean deserializeBytes2Boolean(byte[] value) {
        if (value.length != TypeConstants.BOOLEAN_LENGTH) {
            throw new ProtoException("The length of boolean value MUST BE " + TypeConstants.BOOLEAN_LENGTH + " but now is " + value.length);
        }
        return DeserializeUtils.bytes2Boolean(value);
    }

    @DeserializeValue(flag = TypeConstants.STRING_FLAG)
    public static String deserializeBytes2String(byte[] value) {
        if (value == null || value.length == 0) {
            return null;
        }
        return DeserializeUtils.bytes2String(value);
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
