package cn.wanfeng.sp.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @date: 2024-06-21 12:21
 * @author: luozh
 * @description: 字节数组格式转换工具列
 * @since: 1.0
 */
public class ByteFormatUtils {

    private static final String BYTE_UNITS = "KMGTPE";

    /**
     * 16进制字符串转成byte数组
     */
    public static byte[] hexStringToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

    /**
     * byte数组转成16进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }

    /**
     * byte转二进制字符串
     */
    public static String byteToBinaryString(byte b) {
        String result = "";
        byte a = b;
        for (int i = 0; i < 8; i++) {
            byte c = a;
            a = (byte) (a >> 1);
            a = (byte) (a << 1);
            if (a == c) {
                result = "0" + result;
            } else {
                result = "1" + result;
            }
            a = (byte) (a >> 1);
        }
        return result;
    }

    /**
     * 二进制字符串转byte
     */
    public static byte binaryStringToByte(String bString) {
        byte result = 0;
        for (int i = bString.length() - 1, j = 0; i >= 0; i--, j++) {
            result += (byte) (Byte.parseByte(bString.charAt(i) + "") * Math.pow(2, j));
        }
        return result;

    }

    /**
     * 字节数转换为直观显示的单位，例如 10000000B = 9.54MB
     * @param byteCount 字节数
     * @return 转换单位后的显示,默认保留两位小数
     */
    public static String byteCountToDisplayString(long byteCount){
        if (byteCount < 1024) {
            return byteCount + " B";
        }
        int exp = (int) (Math.log(byteCount) / Math.log(1024));
        String pre = BYTE_UNITS.charAt(exp - 1) + "B";
        double value = byteCount / Math.pow(1024, exp);
        DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US)); // 使用US locale来避免千位分隔符的问题
        return df.format(value) + " " + pre;
    }

}
