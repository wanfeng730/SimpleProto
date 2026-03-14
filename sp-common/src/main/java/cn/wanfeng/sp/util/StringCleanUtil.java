package cn.wanfeng.sp.util;

import org.apache.commons.lang3.StringUtils;

/**
 * StringCleanUtil: desc.
 *
 * @date: 2026-03-14 12:33
 * @author: wanfeng·Aura
 */
public class StringCleanUtil {

    /**
     * 字符串清洗：去空格 + 全角转半角
     */
    public static String clean(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        // 全角 → 半角
        str = fullWidthToHalfWidth(str);
        // 去掉所有空格（包括首尾、中间）
        str = str.replaceAll("\\s+", "");
        return str;
    }

    /**
     * 全角字符转半角字符
     */
    private static String fullWidthToHalfWidth(String fullWidthStr) {
        if (StringUtils.isBlank(fullWidthStr)) {
            return "";
        }

        char[] chars = fullWidthStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= 65281 && c <= 65374) {
                // 全角字符范围
                chars[i] = (char) (c - 65248);
            } else if (c == 12288) {
                // 全角空格 → 半角空格
                chars[i] = ' ';
            }
        }
        return new String(chars);
    }
}
