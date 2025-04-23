package cn.wanfeng.sp.util;


import com.github.f4b6a3.ulid.UlidCreator;

/**
 * UlidUtil: ULID工具.
 *
 * @date: 2025-04-23 20:23
 * @author: luozh.wanfeng
 */
public class UlidUtil {

    /**
     * 生成ULID
     * @return ULID
     */
    public static String generateUlid(){
        return UlidCreator.getUlid().toString();
    }

}
