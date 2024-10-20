package cn.wanfeng.proto.serial;

import cn.wanfeng.proto.value.ProtoValueConstants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @date: 2024-02-07 22:10
 * @author: luozh
 * @since: 1.0
 */
public class SerializeUtils {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static byte[] int2FourBytes(int value) {
        byte[] bytes = new byte[Integer.BYTES];
        bytes[3] = (byte) value;
        bytes[2] = (byte) (value >>> 8);
        bytes[1] = (byte) (value >>> 16);
        bytes[0] = (byte) (value >>> 24);
        return bytes;
    }

    public static byte[] int2TwoBytes(int value) {
        byte[] fourBytes = int2FourBytes(value);
        return new byte[]{fourBytes[2], fourBytes[3]};
    }

    public static byte[] long2Bytes(long value) {
        byte[] bytes = new byte[Long.BYTES];
        bytes[7] = (byte) value;
        bytes[6] = (byte) (value >> 8);
        bytes[5] = (byte) (value >> 16);
        bytes[4] = (byte) (value >> 24);
        bytes[3] = (byte) (value >> 32);
        bytes[2] = (byte) (value >> 40);
        bytes[1] = (byte) (value >> 48);
        bytes[0] = (byte) (value >> 56);
        return bytes;
    }

    public static byte[] double2Bytes(double value) {
        long lv = Double.doubleToRawLongBits(value);
        return long2Bytes(lv);
    }

    public static byte[] date2Bytes(Date value) {
        long millis = value.getTime();
        return long2Bytes(millis);
    }

    public static byte[] boolean2Bytes(boolean value) {
        byte[] bytes = new byte[1];
        bytes[0] = value ? ProtoValueConstants.BOOLEAN_TRUE : ProtoValueConstants.BOOLEAN_FALSE;
        return bytes;
    }

    public static byte[] string2Bytes(String value) {
        return value.getBytes(DEFAULT_CHARSET);
    }

    public static byte[] stringLen2Bytes(int len) {
        byte[] bytes = new byte[2];
        bytes[1] = (byte) (len % 256);
        bytes[0] = (byte) (len / 256);
        return bytes;
    }

    public static byte[] textLen2Bytes(int len) {
        byte[] bytes = new byte[3];

        int n1 = len / (256 * 256);
        bytes[0] = (byte) n1;

        int len2 = len - (n1 * 256 * 256);
        int n2 = len2 / 256;
        bytes[1] = (byte) n2;

        int len3 = len2 - (n2 * 256);
        bytes[2] = (byte) len3;
        return bytes;
    }


}
