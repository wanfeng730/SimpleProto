package cn.wanfeng.sp.config.filestorage;


import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.storage.file.FileStorageClient;
import cn.wanfeng.sp.storage.file.MinIOFileStorage;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @date: 2024-12-15 23:41
 * @author: luozh.wanfeng
 * @description: 文件存储客户端配置
 * @since: 1.0
 */
@Configuration
public class FileStorageClientConfiguration {

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    @Bean
    @Conditional(MinIOFileStorageCondition.class)
    public FileStorageClient minioFileStorageClient(){
        MinIOFileStorage fileStorage = new MinIOFileStorage(
                SimpleProtoConfig.fileStorageEndPoint,
                SimpleProtoConfig.fileStorageAccessKey,
                SimpleProtoConfig.fileStorageSecretKey,
                SimpleProtoConfig.fileStorageBucket
        );
        LogUtil.info(" [SimpleProto初始化] 文件存储客户端完成[type=MinIO, bucket={}]", SimpleProtoConfig.fileStorageBucket);
        return fileStorage;
    }

}
