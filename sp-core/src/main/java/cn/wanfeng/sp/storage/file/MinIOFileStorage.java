package cn.wanfeng.sp.storage.file;


import cn.wanfeng.sp.exception.SpFileStorageException;
import cn.wanfeng.sp.util.InputStreamUtils;
import cn.wanfeng.sp.util.LogUtil;
import io.minio.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * @date: 2024-12-16 00:04
 * @author: luozh.wanfeng
 * @description: MinIO客户端实现文件存储操作
 * @since: 1.0
 */
public class MinIOFileStorage implements FileStorageClient {

    private MinioClient client;

    private String bucketName;

    public MinIOFileStorage(MinioClient client, String bucketName) {
        this.client = client;
        this.bucketName = bucketName;
        assertBucketExists(bucketName);
    }

    public MinIOFileStorage(String endPoint, String accessKey, String secretKey, String bucketName){
        this.client = MinioClient.builder().endpoint(endPoint).credentials(accessKey, secretKey).build();
        this.bucketName = bucketName;
        assertBucketExists(bucketName);
    }


    @Override
    public void assertBucketExists(String bucketName) {
        try {
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            boolean exists = client.bucketExists(bucketExistsArgs);
            if(!exists){
                throw new SpFileStorageException("桶[%s]不存在, 请检查MinIO配置是否正确", bucketName);
            }
        } catch (Exception e) {
            throw new SpFileStorageException(e, "判断桶[%s]是否存在 出现异常", bucketName);
        }

    }

    @Override
    public void setObject(String storageKey, File file) {
        ByteArrayInputStream inputStream = InputStreamUtils.getByteArrayInputStreamFromFile(file);
        setObject(storageKey, inputStream);
    }

    @Override
    public void setObject(String storageKey, InputStream inputStream) {
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(storageKey)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            ObjectWriteResponse response = client.putObject(putObjectArgs);
            LogUtil.debug("上传文件到MinIO成功，路径={}", storageKey);
        } catch (Exception e) {
            LogUtil.error("上传文件到MinIO失败[bucketName={}, storageKey={}]", bucketName, storageKey, e);
        }
    }

    @Override
    public InputStream getObjectStream(String storageKey) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucketName).object(storageKey).build();
            GetObjectResponse response = client.getObject(getObjectArgs);
            byte[] objectBytes = response.readAllBytes();
            return new ByteArrayInputStream(objectBytes);
        } catch (Exception e){
            LogUtil.error("从MinIO获取文件失败[bucketName={}, storageKey={}]", bucketName, storageKey, e);
            return null;
        }

    }

    @Override
    public File downloadObject(String storageKey, String targetFilePath) {
        return null;
    }

    @Override
    public void removeObject(String storageKey) {

    }
}
