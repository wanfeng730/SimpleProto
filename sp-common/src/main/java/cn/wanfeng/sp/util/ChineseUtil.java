package cn.wanfeng.sp.util;


import com.github.promeg.pinyinhelper.Pinyin;
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

    /**
     * 获取全拼大写
     *
     * @param chinese 中文
     * @return 全拼
     */
    public static String getPinyin(String chinese) {
        return getPinyin(chinese, ""); // 第二个参数是分隔符（空字符串表示无分隔）
    }

    /**
     * 获取全拼大写
     *
     * @param chinese   中文
     * @param separator 分隔符
     * @return 全拼
     */
    public static String getPinyin(String chinese, String separator) {
        return Pinyin.toPinyin(chinese, separator);
    }


    // 获取首字母大写
    public static String getPinyinHeader(String chinese) {
        StringBuilder headers = new StringBuilder();
        for (char c : chinese.toCharArray()) {
            if (Pinyin.isChinese(c)) {
                String pinyin = Pinyin.toPinyin(c);
                headers.append(pinyin.charAt(0)); // 取拼音首字母
            } else {
                headers.append(c);
            }
        }
        return headers.toString();
    }

}
