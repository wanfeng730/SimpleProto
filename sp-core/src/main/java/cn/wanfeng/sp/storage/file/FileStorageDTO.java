package cn.wanfeng.sp.storage.file;

import lombok.Data;

import java.util.Date;

/**
 * FileStorageDTO: 文件存储对象.
 *
 * @date: 2025-06-05 13:00
 * @author: luozh.wanfeng
 */
@Data
public class FileStorageDTO {

    private String name;

    private String storageKey;

    private Date modifyDate;

    private long size;
}
