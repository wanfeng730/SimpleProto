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
     * 计算两个数的百分比数值，保留2位小数，小于0.01%时显示小于号
     * @param d1 分子
     * @param d2 分母
     * @return 百分比数值
     */
    public static String calPercentNumberScale2(Double d1, Double d2){
        double percentValue = BigDecimal.valueOf(d1).divide(BigDecimal.valueOf(d2), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP).doubleValue();
        return percentValue < 0.01 ? "<0.01" : percentValue + "";
    }
}
