package cn.wanfeng.sp.util;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ChineseUtils: 中文文本工具类.
 *
 * @date: 2025-04-15 23:22
 * @author: luozh.wanfeng
 */
public class ChineseUtil {

    /**
     * 字符串是否包含中文
     *
     * @param str 待校验字符串
     * @return true 包含中文字符 false 不包含中文字符
     */
    public static boolean isContainChinese(String str)  {
        if (StringUtils.isBlank(str)) {
            return true;
        }
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

}
