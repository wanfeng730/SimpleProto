package cn.wanfeng.sp.storage.file;


import cn.wanfeng.sp.exception.SimpleExceptionCode;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.exception.SpFileStorageException;
import cn.wanfeng.sp.util.DateUtils;
import cn.wanfeng.sp.util.FileUtils;
import cn.wanfeng.sp.util.InputStreamUtils;
import cn.wanfeng.sp.util.LogUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @date: 2024-12-16 00:04
 * @author: luozh.wanfeng
 * @description: MinIO客户端实现文件存储操作
 * @since: 1.0
 */
public class MinIOFileStorage implements FileStorageClient {

    private String endPoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    private MinioClient client;

    public MinIOFileStorage(MinioClient client, String bucketName) {
        this.client = client;
        this.bucketName = bucketName;
        assertBucketExists(bucketName);
    }

    public MinIOFileStorage(String endPoint, String accessKey, String secretKey, String bucketName){
        this.endPoint = endPoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.client = MinioClient.builder().endpoint(endPoint).credentials(accessKey, secretKey).build();
        assertBucketExists(bucketName);
    }


    @Override
    public void assertBucketExists(String bucketName) {
        try {
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            boolean exists = client.bucketExists(bucketExistsArgs);
            if(!exists){
                throw new SpException(SimpleExceptionCode.FILE_STORAGE_BUCKET_NOT_FOUND, bucketName);
            }
        } catch (Exception e) {
            throw new SpFileStorageException(e, "判断桶[%s]是否存在 出现异常", bucketName);
        }
    }

    /**
     * 获取存储文件列表
     *
     * @param prefix 路径前缀
     * @return 文件列表
     */
    @Override
    public List<FileStorageDTO> listObject(String prefix) {
        List<FileStorageDTO> fileStorageDTOList = new ArrayList<>();
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName)
                .prefix(prefix)
                .recursive(true)
                .includeVersions(true)
                .build();
        try {
            Iterable<Result<Item>> resultIterable = client.listObjects(listObjectsArgs);
            for (Result<Item> result : resultIterable) {
                String objectName = result.get().objectName();
                long size = result.get().size();
                LocalDateTime modifyDateLocal = result.get().lastModified().toLocalDateTime();

                FileStorageDTO dto = new FileStorageDTO();
                dto.setName(FileUtils.getName(objectName));
                dto.setStorageKey(objectName);
                dto.setSize(size);
                dto.setModifyDate(DateUtils.toDate(modifyDateLocal));
                fileStorageDTOList.add(dto);
            }
        } catch (Exception e){
            throw new SpException(e, "获取文件存储列表失败 prefix: %s", prefix);
        }
        return fileStorageDTOList;
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
            throw new SpFileStorageException(e, "从MinIO获取文件失败[bucketName=%s, storageKey=%s]", bucketName, storageKey);
        }
    }

    @Override
    public String getObjectPreviewUrl(String storageKey, int expireSeconds) {
        try {
            GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(storageKey)
                    .expiry(expireSeconds)
                    .method(Method.GET)
                    .build();
            return client.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        } catch (Exception e) {
            LogUtil.error("从MinIO获取文件预览链接失败 [bucketName={}, storageKey={}]", bucketName, storageKey, e);
        }
        return null;
    }

    /**
     * 获取文件预览链接（永久）
     *
     * @param storageKey 文件存储路径
     * @return url
     */
    @Override
    public String getObjectPreviewUrl(String storageKey, boolean proxyHost) {
        String host = proxyHost ? "/minio" : endPoint;
        return host + "/" + bucketName + "/" + StringUtils.removeStart(storageKey, '/');
    }

    @Override
    public File downloadObject(String storageKey, String targetFilePath) {
        File file = FileUtils.touch(targetFilePath);
        InputStream inputStream = getObjectStream(storageKey);
        return ObjectUtils.isEmpty(inputStream) ? null : FileUtils.writeFromStream(inputStream, file);
    }

    @Override
    public File downloadObject(String storageKey) {
        String tempDirectoryPath = FileUtils.createTempDirectory("minio_").getPath();
        String filename = FileUtils.getName(storageKey);
        String targetFilePath = tempDirectoryPath + File.separator + filename;
        return downloadObject(storageKey, targetFilePath);
    }

    @Override
    public void removeObject(String storageKey) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucketName).object(storageKey).build();
            client.removeObject(removeObjectArgs);
        } catch (Exception e) {
            throw new SpFileStorageException(e, "从MinIO删除文件失败[bucketName=%s, storageKey=%s]", bucketName, storageKey);
        }
    }

    /**
     * 复制文件对象到新路径
     *
     * @param sourceKey 原文件存储路径
     * @param targetKey 复制目标存储路径
     */
    @Override
    public void copyObject(String sourceKey, String targetKey) {
        try {
            CopySource copySource = CopySource.builder().bucket(bucketName).object(sourceKey).build();
            CopyObjectArgs copyObjectArgs = CopyObjectArgs.builder().source(copySource).bucket(bucketName).object(targetKey).build();
            client.copyObject(copyObjectArgs);
        } catch (Exception e) {
            throw new SpFileStorageException(e, "MinIO复制文件失败[bucketName=%s, sourceKey=%s, targetKey=%s]", bucketName, sourceKey, targetKey);
        }
    }

    /**
     * 移动文件对象到新路径
     *
     * @param sourceKey 原文件存储路径
     * @param targetKey 目标存储路径
     */
    @Override
    public void moveObject(String sourceKey, String targetKey) {
        try {
            copyObject(sourceKey, targetKey);
            removeObject(sourceKey);
        } catch (Exception e) {
            throw new SpFileStorageException(e, "MinIO移动文件失败[bucketName=%s, sourceKey=%s, targetKey=%s]", bucketName, sourceKey, targetKey);
        }
    }
}
