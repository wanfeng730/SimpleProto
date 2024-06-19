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

    SMALL_INT(TypeConstants.SMALL_INT_FLAG, Byte.class, TypeConstants.SMALL_INT_LENGTH),
    INT(TypeConstants.INT_FLAG, Integer.class, TypeConstants.INT_LENGTH),
    LONG(TypeConstants.LONG_FLAG, Long.class, TypeConstants.LONG_LENGTH),
    DOUBLE(TypeConstants.DOUBLE_FLAG, Double.class, TypeConstants.DOUBLE_LENGTH),
    BOOLEAN(TypeConstants.BOOLEAN_FLAG, Boolean.class, TypeConstants.BOOLEAN_LENGTH),
    DATE(TypeConstants.DATE_FLAG, Date.class, TypeConstants.DATE_LENGTH),
    STRING(TypeConstants.STRING_FLAG, String.class, TypeConstants.STRING_MAX_LENGTH);

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
}
