package cn.wanfeng.sp.util;

import org.ofdrw.reader.OFDReader;

/**
 * OfdPageUtil: ofd文件页工具类.
 *
 * @date: 2025-08-08 10:54
 * @author: luozh.wanfeng
 */
public class OfdPageUtil {

    /**
     * 获取ofd文件总页数
     *
     * @param ofdFilePath ofd文件路径
     * @return 总页数
     */
    public static int getTotalPage(String ofdFilePath) {
        try (OFDReader reader = new OFDReader(ofdFilePath)) {
            return reader.getPageList().size();
        } catch (Exception e) {
            LogUtil.error("获取ofd文件总页数失败 ofdFilePath={}", ofdFilePath, e);
            return -1;
        }
    }
}
