package cn.wanfeng.sp.exception;


/**
 * @date: 2024-12-15 23:50
 * @author: luozh.wanfeng
 * @description: SimpleProto配置异常
 * @since: 1.0
 */
public class SimpleProtoConfigException extends SpException{

    public SimpleProtoConfigException(String message) {
        super(message);
    }


    public static SimpleProtoConfigException fileStorageNotBeOptionValue(){
        return new SimpleProtoConfigException("Config[simpleproto.fileStorageType] Can not be Blank, Please Choose a Value From [MinIO, ]");
    }
}
