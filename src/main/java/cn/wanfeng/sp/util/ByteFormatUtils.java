package cn.wanfeng.sp.util;

/**
 * @date: 2024-06-21 12:21
 * @author: luozh
 * @description: 字节数组格式转换工具列
 * @since: 1.0
 */
public class ByteFormatUtils {

    /**
     * 简洁写法 16进制字符串转成byte数组
     *
     * @param hex 16进制字符串，支持大小写
     * @return byte数组
     */
    public static byte[] hexStringToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

    /**
     * 简洁写法 byte数组转成16进制字符串
     *
     * @param bytes byte数组
     * @return 16进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }

}
