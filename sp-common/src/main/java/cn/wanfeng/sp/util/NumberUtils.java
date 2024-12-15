package cn.wanfeng.sp.util;


/**
 * @date: 2024-12-15 18:19
 * @author: luozh.wanfeng
 * @description: 数字工具类
 * @since: 1.0
 */
public class NumberUtils {

    public static String generateNumberFixLength(Long num, int length){
        return String.format("%0"+length+"d", num);
    }


}
