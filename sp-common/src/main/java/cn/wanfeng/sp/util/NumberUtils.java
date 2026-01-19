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

    // 小写中文数字
    private static final String[] CN_LOWER = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    // 大写中文数字(财务专用)
    private static final String[] CN_UPPER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    // 单位
    private static final String[] UNITS = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};




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



    /**
     * 数字转换为汉字小写
     * @param value 数字
     * @return 汉字
     */
    public static String toChineseLower(Integer value){
        return toChinese(value, false);
    }

    /**
     * 数字转换为汉字大写（金额）
     * @param value 数字
     * @return 汉字
     */
    public static String toChineseUpper(Integer value){
        return toChinese(value, true);
    }

    /**
     * 数字转换为汉字
     * @param num 数字
     * @param isUpper 是否使用大写汉字
     * @return 汉字
     */
    private static String toChinese(Integer num, boolean isUpper){
        if (num == 0) {
            return isUpper ? CN_UPPER[0] : CN_LOWER[0];
        }
        String[] nums = isUpper ? CN_UPPER : CN_LOWER;
        boolean negative = num < 0;
        num = Math.abs(num);
        char[] numChars = String.valueOf(num).toCharArray();
        StringBuilder sb = new StringBuilder();
        int len = numChars.length;
        for (int i = 0; i < len; i++) {
            int n = numChars[i] - '0';
            int unitIndex = len - 1 - i;
            if (n != 0) {
                sb.append(nums[n]).append(UNITS[unitIndex]);
            } else {
                if (!sb.isEmpty() && !sb.substring(sb.length() - 1).equals(nums[0])) {
                    sb.append(nums[0]);
                }
            }
        }
        // 处理末尾的零
        String result = sb.toString().replaceAll(nums[0] + "$", "");
        // 处理负数
        if (negative) {
            result = "负" + result;
        }
        // 处理 一十 → 十
        if (result.startsWith(nums[1] + UNITS[1]) && result.length() == 2) {
            result = result.substring(1);
        }
        return result;
    }
}
