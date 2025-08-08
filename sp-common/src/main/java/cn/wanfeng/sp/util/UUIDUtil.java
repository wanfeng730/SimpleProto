package cn.wanfeng.sp.util;

import java.util.UUID;

/**
 * UUIDUtil: UUID工具类.
 *
 * @date: 2025-08-08 16:39
 * @author: luozh.wanfeng
 */
public class UUIDUtil {

    /**
     * 字符串是否为uuid
     * @param str 字符串
     * @return 是否为uuid
     */
    public static boolean isUUID(String str) {
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
