package cn.wanfeng.sp.util;


import cn.wanfeng.sp.exception.SpException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RandomPasswordUtil: 随机密码工具.
 *
 * @date: 2025-08-16 17:28
 * @author: luozh.wanfeng
 */
public class RandomPasswordUtil {

    private static final String DIGITS = "0123456789";
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALL_CHARS = DIGITS + UPPER_CASE + LOWER_CASE;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成随机密码 只包含数字、大小写字母
     *
     * @param length 位数
     * @return 随机密码
     */
    public static String generatePassword(int length) {
        if (length <= 0) {
            throw new SpException("RANDOM_PASSWORD_LENGTH_LOWER_ZERO", "生成随机密码的位数不能小于等于0 length: %d", length);
        }
        // 确保密码包含至少一个数字、一个大写字母和一个小写字母
        char[] password = new char[length];
        // 填充剩余3位
        for (int i = 0; i < length; i++) {
            password[i] = ALL_CHARS.charAt(RANDOM.nextInt(ALL_CHARS.length()));
        }
        // 打乱字符顺序（确保随机性）
        return shufflePassword(password);
    }

    /**
     * 打乱字符顺序
     *
     * @param password 密码文本
     * @return 打乱文本
     */
    private static String shufflePassword(char[] password) {
        List<Character> list = new ArrayList<>();
        for (char c : password) {
            list.add(c);
        }
        Collections.shuffle(list, RANDOM);

        StringBuilder sb = new StringBuilder();
        for (char c : list) {
            sb.append(c);
        }
        return sb.toString();
    }
}
