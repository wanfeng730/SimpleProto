package cn.wanfeng.sp.proto.type;

/**
 * @date: 2024-02-06 23:20
 * @author: luozh
 * @since: 1.0
 */
public final class TypeConstants {
    public static final byte SMALL_INT_FLAG = 1;
    public static final byte INT_FLAG = 2;
    public static final byte LONG_FLAG = 3;
    public static final byte DOUBLE_FLAG = 4;
    public static final byte BOOLEAN_FLAG = 5;
    public static final byte DATE_FLAG = 6;
    public static final byte STRING_FLAG = -128;

    public static final int SMALL_INT_LENGTH = Byte.BYTES;
    public static final int INT_LENGTH = Integer.BYTES;
    public static final int LONG_LENGTH = Long.BYTES;
    public static final int DOUBLE_LENGTH = Double.BYTES;
    public static final int BOOLEAN_LENGTH = Byte.BYTES;
    public static final int DATE_LENGTH = Long.BYTES;

    public static final int STRING_MAX_LENGTH = 63 * 256 + 255;

}
