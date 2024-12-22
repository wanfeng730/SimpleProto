package cn.wanfeng.sp.api.enums;

/**
 * @date: 2024-12-22 15:40
 * @author: luozh.wanfeng
 * @description: 存储容量单位
 * @since: 1.0
 */
public enum ByteUnit {
    B("B", 1L),
    KB("KB", 1024L),
    MB("MB", 1024L * 1024L),
    GB("GB", 1024L * 1024L * 1024L),
    TB("TB", 1024L * 1024L * 1024L * 1024L);

    private String displayName;

    private long byteCount;

    ByteUnit(String displayName, long byteCount) {
        this.displayName = displayName;
        this.byteCount = byteCount;
    }
}
