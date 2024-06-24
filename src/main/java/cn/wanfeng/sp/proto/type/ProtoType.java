package cn.wanfeng.sp.proto.type;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @date: 2024-02-06 23:27
 * @author: luozh
 * @since: 1.0
 */
public enum ProtoType {

    SMALL_INT(ProtoTypeConstants.SMALL_INT_FLAG, Byte.class, ProtoTypeConstants.SMALL_INT_LENGTH),
    INT(ProtoTypeConstants.INT_FLAG, Integer.class, ProtoTypeConstants.INT_LENGTH),
    LONG(ProtoTypeConstants.LONG_FLAG, Long.class, ProtoTypeConstants.LONG_LENGTH),
    DOUBLE(ProtoTypeConstants.DOUBLE_FLAG, Double.class, ProtoTypeConstants.DOUBLE_LENGTH),
    BOOLEAN(ProtoTypeConstants.BOOLEAN_FLAG, Boolean.class, ProtoTypeConstants.BOOLEAN_LENGTH),
    DATE(ProtoTypeConstants.DATE_FLAG, Date.class, ProtoTypeConstants.DATE_LENGTH),
    STRING(ProtoTypeConstants.STRING_FLAG, String.class, ProtoTypeConstants.STRING_MAX_LENGTH),
    TEXT(ProtoTypeConstants.TEXT_FLAG, Text.class, ProtoTypeConstants.TEXT_MAX_LENGTH);

    private final byte flag;
    private final Class<?> typeClass;
    private final int len;

    ProtoType(byte flag, Class<?> typeClass, Integer len) {
        this.flag = flag;
        this.typeClass = typeClass;
        this.len = len;
    }

    public byte toFlag() {
        return this.flag;
    }

    public Class<?> toClass() {
        return this.typeClass;
    }

    public int toLen() {
        return this.len;
    }

    public boolean equals(ProtoType type) {
        return type.flag == this.flag;
    }

    public static Map<Byte, Class<?>> toClassMap() {
        return Arrays.stream(ProtoType.values()).collect(Collectors.toMap(ProtoType::toFlag, ProtoType::toClass));
    }

    public static Map<Byte, ProtoType> toEnumMap() {
        return Arrays.stream(ProtoType.values()).collect(Collectors.toMap(ProtoType::toFlag, protoType -> protoType));
    }

    public static Map<Byte, Integer> toLengthMap() {
        return Arrays.stream(ProtoType.values()).collect(Collectors.toMap(ProtoType::toFlag, ProtoType::toLen));
    }

    public static Map<Class<?>, Integer> toClassLengthMap() {
        return Arrays.stream(ProtoType.values()).collect(Collectors.toMap(ProtoType::toClass, ProtoType::toLen));
    }

    public static Map<Class<?>, ProtoType> toClassEnumMap() {
        return Arrays.stream(ProtoType.values()).collect(Collectors.toMap(ProtoType::toClass, protoType -> protoType));
    }
}
