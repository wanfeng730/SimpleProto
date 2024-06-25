package cn.wanfeng.sp.proto.type;

/**
 * @date: 2024-02-15 22:38
 * @author: luozh
 * @since: 1.0
 */
public class ProtoTypeUtils {

    // 00100000
    private static final byte IS_EMPTY_MAT = 32;

    // 11011111
    private static final byte FLAG_MAT = -33;

    // 11000000
    private static final byte STRING_TYPE_MAT = -64;

    // 00011111
    private static final byte FLAG_BYTE_LENGTH_MAT = 31;

    public static boolean isEmptyValue(byte typeByte) {
        return (typeByte & IS_EMPTY_MAT) == IS_EMPTY_MAT;
    }

    public static byte type0SetEmpty(byte type0) {
        return (byte) (type0 | IS_EMPTY_MAT);
    }

    /**
     * 从type的第一个字节中获取flag，若为文本类型则需要根据文本类型垫子STRING_TYPE_MAT获取
     */
    public static byte getTypeFlagFromData1(byte data1) {
        byte typeFlag = (byte) (data1 & FLAG_MAT);
        if (typeFlag < 0) {
            typeFlag = (byte) (typeFlag & STRING_TYPE_MAT);
        }
        return typeFlag;
    }

    public static int getFlagByteLengthFromData1(byte data1) {
        return data1 & FLAG_BYTE_LENGTH_MAT;
    }

}
