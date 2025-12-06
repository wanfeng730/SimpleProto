package cn.wanfeng.sp.exception;


/**
 * @date: 2024-12-19 23:47
 * @author: luozh.wanfeng
 * @description: 文件对象存储异常
 * @since: 1.0
 */
public class SpFileStorageException extends SpException{

    public SpFileStorageException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }


    public SpFileStorageException(String message) {
        super(message);
    }
}
