package cn.wanfeng.sp.storage.file;


import io.minio.MinioClient;

import java.io.File;
import java.io.InputStream;

/**
 * @date: 2024-12-16 00:04
 * @author: luozh.wanfeng
 * @description: MinIO客户端实现文件存储操作
 * @since: 1.0
 */
public class MinIOFileStorage implements FileStorageClient{

    private MinioClient client;

    @Override
    public void setObject(String storageKey, File file) {

    }

    @Override
    public void setObject(String storageKey, InputStream inputStream) {

    }

    @Override
    public InputStream getObjectStream(String storageKey) {
        return null;
    }

    @Override
    public File downloadObject(String storageKey, String targetFilePath) {
        return null;
    }
}
