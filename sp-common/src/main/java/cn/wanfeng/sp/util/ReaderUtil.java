package cn.wanfeng.sp.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

/**
 * ReaderUtil: desc.
 *
 * @date: 2026-03-14 11:39
 * @author: wanfeng·Aura
 */
public class ReaderUtil {
    /**
     * 优雅关闭reader
     * @param reader 流
     */
    public static void closeQuietly(Reader reader) {
        if (Objects.isNull(reader)) {
            return;
        }
        try {
            reader.close();
        } catch (IOException e) {
            LogUtil.error("关闭Reader失败", e);
        }
    }
}
