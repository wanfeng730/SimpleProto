package cn.wanfeng.proto.util;

/**
 * @date: 2024-02-15 22:27
 * @author: luozh
 * @since: 1.0
 */
public class ByteArrayUtils {

    /**
     * 从arr的头部消费（删除）length个元素
     */
    public static byte[] consumeByteArrayHead(byte[] arr, int length) {
        int newLen = arr.length - length;
        byte[] newArr = new byte[newLen];
        System.arraycopy(arr, length, newArr, 0, newLen);
        return newArr;
    }

    /**
     * 读取arr的子串，从start位置开始，读取length长度
     */
    public static byte[] subByteArray(byte[] arr, int start, int length) {
        byte[] newArr = new byte[length];
        System.arraycopy(arr, start, newArr, 0, length);
        return newArr;
    }

}
