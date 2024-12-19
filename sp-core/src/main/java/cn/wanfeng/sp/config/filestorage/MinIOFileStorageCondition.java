package cn.wanfeng.sp.config.filestorage;


import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SimpleProtoConfigException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @date: 2024-12-15 23:44
 * @author: luozh.wanfeng
 * @since: 1.0
 */
public class MinIOFileStorageCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String type = context.getEnvironment().getProperty("simpleproto.fileStorageType");
        if(StringUtils.isBlank(type)){
            throw SimpleProtoConfigException.fileStorageNotBeOptionValue();
        }
        return StringUtils.equals(SimpleProtoConfig.FILE_STORAGE_TYPE_MINIO, type);
    }
}
