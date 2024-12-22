package cn.wanfeng.sp.api.domain;


import cn.wanfeng.sp.api.enums.FileTag;

import java.io.File;
import java.io.InputStream;

/**
 * @date: 2024-12-22 16:22
 * @author: luozh.wanfeng
 * @since: 1.0
 */
public interface ISpFile extends ISpSysObject{

    String FILE_TAG_FIELD = "file_tag";
    String SUFFIX_FIELD = "suffix";
    String FILE_SIZE_FIELD = "file_size";
    String FILE_STORAGE_KEY_FIELD = "file_storage_key";

    int FILE_TAG_INDEX = 1011;
    int SUFFIX_INDEX = 1012;
    int FILE_SIZE_INDEX = 1013;
    int FILE_STORAGE_KEY_INDEX = 1014;

    /**
     * 设置文件类型
     * @param fileTag 文件类型
     */
    void setFileTag(FileTag fileTag);

    void setStorage(File file);

    void setStorage(String storageKey);

    void setStorage(InputStream inputStream);

    /**
     * 获取文件大小
     * @return 字节数
     */
    long getSize();

    /**
     * 获取文件后缀名
     * @return 后缀名
     */
    String getSuffix();

    /**
     * 获取存储路径key
     * @return 存储路径
     */
    String getStorageKey();

    /**
     * 获取文件流
     * @return 文件流
     */
    InputStream getInputStream();

    /**
     * 下载文件到本地
     * @param targetFilePath 本地文件路径
     * @return 下载后的文件
     */
    File download(String targetFilePath);
}
