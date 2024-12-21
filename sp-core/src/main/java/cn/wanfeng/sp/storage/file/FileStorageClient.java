package cn.wanfeng.sp.storage.file;

import java.io.File;
import java.io.InputStream;

/**
 * @date: 2024-12-15 23:42
 * @author: luozh.wanfeng
 * @description: 文件对象存储客户端接口
 * @since: 1.0
 */
public interface FileStorageClient {

    /**
     * 桶是否存在
     * @param bucketName 桶名
     */
    void assertBucketExists(String bucketName);

    /**
     * 上传文件对象
     * @param storageKey 文件存储路径
     * @param file 本地文件
     */
    void setObject(String storageKey, File file);

    /**
     * 上传文件对象
     * @param storageKey 文件存储路径
     * @param inputStream 文件流（建议使用ByteArrayInputStream）
     */
    void setObject(String storageKey, InputStream inputStream);

    /**
     * 获取文件对象的流
     * @param storageKey 文件存储路径
     * @return 文件流
     */
    InputStream getObjectStream(String storageKey);

    /**
     * 获取文件预览链接
     * @param storageKey 文件存储路径
     * @param expireSeconds 有效期（秒）
     * @return url
     */
    String getObjectPreviewUrl(String storageKey, int expireSeconds);

    /**
     * 下载文件对象到本地
     * @param storageKey 文件存储路径
     * @param targetFilePath 下载到这个路径
     * @return 文件流
     */
    File downloadObject(String storageKey, String targetFilePath);

    /**
     * 下载文件对象到本地随机路径
     * @param storageKey 文件存储路径
     * @return 文件流
     */
    File downloadObject(String storageKey);

    /**
     * 删除文件对象
     * @param storageKey 文件存储路径
     */
    void removeObject(String storageKey);

}
