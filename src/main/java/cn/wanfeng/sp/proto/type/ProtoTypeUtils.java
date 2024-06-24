package cn.wanfeng.sp.proto.type;

/**
 * @date: 2024-02-15 22:38
 * @author: luozh
 * @since: 1.0
 */
public class ProtoTypeUtils {

    // 00100000
    private static final byte IS_EMPTY_MAT = 32;

    // 11000000
    private static final byte FLAG_MAT = 63;

    // 00011111
    private static final byte FLAG_BYTE_LENGTH_MAT = 31;

    public static boolean isEmptyValue(byte typeByte) {
        return (typeByte & IS_EMPTY_MAT) == IS_EMPTY_MAT;
    }

    public static byte type0SetEmpty(byte type0) {
        return (byte) (type0 | IS_EMPTY_MAT);
    }

    public static byte getTypeFlagFromData1(byte data1) {
        return (byte) (data1 & FLAG_MAT);
    }

    public static int getFlagByteLengthFromData1(byte data1) {
        return data1 & FLAG_BYTE_LENGTH_MAT;
    }

}
