package cn.wanfeng.proto.type;

import java.util.Map;

/**
 * @date: 2024-02-07 11:19
 * @author: luozh
 * @since: 1.0
 */
public final class TypeMapContainer {

    public static final Map<Byte, Class<?>> FLAG_CLASS_MAP = ProtoType.toClassMap();
    public static final Map<Byte, ProtoType> FLAG_ENUM_MAP = ProtoType.toEnumMap();
    public static final Map<Byte, Integer> FLAG_LENGTH_MAP = ProtoType.toLengthMap();
    public static final Map<Class<?>, Integer> CLASS_LENGTH_MAP = ProtoType.toClassLengthMap();
    public static final Map<Class<?>, ProtoType> CLASS_ENUM_MAP = ProtoType.toClassEnumMap();

}
