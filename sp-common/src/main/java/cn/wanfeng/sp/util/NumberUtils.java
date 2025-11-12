package cn.wanfeng.sp.util;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

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


    /**
     * double转string
     * @param value 值
     * @param scale 保留小数位数
     * @param roundingMode 取舍枚举
     * @return string
     */
    public static String doubleToString(Double value, int scale, RoundingMode roundingMode){
        if(Objects.isNull(value)){
            return null;
        }
        BigDecimal decimal = new BigDecimal(value);
        decimal = decimal.setScale(scale, roundingMode);
        return decimal.toString();
    }

    /**
     * double转string，保留两位小数、四舍五入
     * @param value 值
     * @return string
     */
    public static String doubleToString(Double value){
        return doubleToString(value, 2, RoundingMode.HALF_UP);
    }
}
