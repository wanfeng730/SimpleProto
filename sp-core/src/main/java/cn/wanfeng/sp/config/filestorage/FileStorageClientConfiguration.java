package cn.wanfeng.sp.config.filestorage;


import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.storage.file.FileStorageClient;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @date: 2024-12-15 23:41
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Configuration
public class FileStorageClientConfiguration {

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    @Bean
    @Conditional(MinIOFileStorageCondition.class)
    public FileStorageClient minioFileStorageClient(){

    }

}
