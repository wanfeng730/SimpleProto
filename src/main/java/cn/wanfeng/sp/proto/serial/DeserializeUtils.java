package cn.wanfeng.sp.proto.serial;

import cn.wanfeng.sp.proto.value.ProtoValueConstants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @date: 2024-02-07 22:36
 * @author: luozh
 * @since: 1.0
 */
public class DeserializeUtils {

    private static final int INT_MAT = 0xFF;
    private static final long LONG_MAT = 0xFF;

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static int oneByte2Int(byte b) {
        byte[] data = new byte[]{0, 0, 0, b};
        return bytes2Int(data);
    }

    public static int bytes2Int(byte[] data) {
        int value = 0;
        value = value | (data[0] & INT_MAT);
        value = (value << 8) | (data[1] & INT_MAT);
        value = (value << 8) | (data[2] & INT_MAT);
        value = (value << 8) | (data[3] & INT_MAT);
        return value;
    }

    public static long bytes2Long(byte[] data) {
        long value = 0;
        value = value | (data[0] & LONG_MAT);
        value = (value << 8) | (data[1] & LONG_MAT);
        value = (value << 8) | (data[2] & LONG_MAT);
        value = (value << 8) | (data[3] & LONG_MAT);
        value = (value << 8) | (data[4] & LONG_MAT);
        value = (value << 8) | (data[5] & LONG_MAT);
        value = (value << 8) | (data[6] & LONG_MAT);
        value = (value << 8) | (data[7] & LONG_MAT);
        return value;
    }

    public static double bytes2Double(byte[] data) {
        long lv = bytes2Long(data);
        return Double.longBitsToDouble(lv);
    }

    public static Date bytes2Date(byte[] data) {
        long millis = bytes2Long(data);
        return new Date(millis);
    }

    public static boolean bytes2Boolean(byte[] data) {
        return data[0] == ProtoValueConstants.BOOLEAN_TRUE ? Boolean.TRUE : Boolean.FALSE;
    }

    public static String bytes2String(byte[] data) {
        return new String(data, DEFAULT_CHARSET);
    }
}
