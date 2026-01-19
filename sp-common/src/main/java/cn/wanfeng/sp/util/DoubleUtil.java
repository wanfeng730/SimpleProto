package cn.wanfeng.sp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * DoubleUtil: desc.
 *
 * @date: 2026-01-14 17:24
 * @author: wanfeng·Aura
 */
public class DoubleUtil {

    /**
     * 计算两个数的百分比数值用于显示，保留2位小数，小于0.01%时显示小于号
     * @param d1 分子
     * @param d2 分母
     * @return 百分比数值
     */
    public static String calDisplayPercentScale2(Double d1, Double d2){
        double percentValue = BigDecimal.valueOf(d1).divide(BigDecimal.valueOf(d2), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP).doubleValue();
        return percentValue < 0.01 ? "<0.01" : percentValue + "";
    }

    /**
     * 计算除法 四舍五入
     * @param d1 分子
     * @param d2 分母
     * @param scale 保留小数位数
     * @return String
     */
    public static Double divide(Double d1, Double d2, int scale){
        return divide(d1, d2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 计算除法
     * @param d1 分子
     * @param d2 分母
     * @param scale 保留小数位数
     * @param roundingMode 保留模式
     * @return String
     */
    public static Double divide(Double d1, Double d2, int scale, RoundingMode roundingMode){
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.divide(b2, scale, roundingMode).doubleValue();
    }
}
