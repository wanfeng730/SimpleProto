package cn.wanfeng.sp.proto.serial;

import cn.wanfeng.sp.proto.type.TypeConstants;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @date: 2024-02-16 12:45
 * @author: luozh
 * @since: 1.0
 */
public class SerializeMethodContainer {

    @SerializeValue(flag = TypeConstants.SMALL_INT_FLAG)
    public static byte[] serializeSmallInt2Bytes(Byte value) {
        if (value == null) {
            return null;
        }
        return new byte[]{value};
    }

    @SerializeValue(flag = TypeConstants.INT_FLAG)
    public static byte[] serializeInt2Bytes(Integer value) {
        if (value == null) {
            return null;
        }
        return SerializeUtils.int2Bytes(value);
    }

    @SerializeValue(flag = TypeConstants.LONG_FLAG)
    public static byte[] serializeLong2Bytes(Long value) {
        if (value == null) {
            return null;
        }
        return SerializeUtils.long2Bytes(value);
    }

    @SerializeValue(flag = TypeConstants.DOUBLE_FLAG)
    public static byte[] serializeDouble2Bytes(Double value) {
        if (value == null) {
            return null;
        }
        return SerializeUtils.double2Bytes(value);
    }

    @SerializeValue(flag = TypeConstants.DATE_FLAG)
    public static byte[] serializeDate2Bytes(Date value) {
        if (value == null) {
            return null;
        }
        return SerializeUtils.date2Bytes(value);
    }

    @SerializeValue(flag = TypeConstants.BOOLEAN_FLAG)
    public static byte[] serializeBoolean2Bytes(Boolean value) {
        if (value == null) {
            return null;
        }
        return SerializeUtils.boolean2Bytes(value);
    }

    @SerializeValue(flag = TypeConstants.STRING_FLAG)
    public static byte[] serializeString2Bytes(String value) {
        if (value == null) {
            return null;
        }
        return SerializeUtils.string2Bytes(value);
    }

    public static Map<Byte, Method> toFlagMethodMap() {
        Map<Byte, Method> methodMap = new HashMap<>();
        Arrays.stream(SerializeMethodContainer.class.getMethods()).forEach(method -> {
            if (method.isAnnotationPresent(SerializeValue.class)) {
                byte flag = method.getAnnotation(SerializeValue.class).flag();
                methodMap.put(flag, method);
            }
        });
        return methodMap;
    }
}
