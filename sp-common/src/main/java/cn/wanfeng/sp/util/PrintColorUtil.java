package cn.wanfeng.sp.util;


/**
 * PrintColorUtil: 改变打印颜色的工具类.
 *
 * @date: 2025-05-18 01:53
 * @author: luozh.wanfeng
 */
public class PrintColorUtil {

    private static final String END = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    public static String cyan(String message) {
        return CYAN + message + END;
    }


}
