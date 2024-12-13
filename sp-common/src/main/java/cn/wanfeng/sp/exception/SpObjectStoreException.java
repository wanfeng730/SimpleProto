package cn.wanfeng.sp.exception;


/**
 * @date: 2024-12-14 00:57
 * @author: luozh.wanfeng
 * @description: 对象存储异常
 * @since: 1.0
 */
public class SpObjectStoreException extends SpException{

    public SpObjectStoreException(Throwable cause) {
        super(cause);
    }

    public SpObjectStoreException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

    public SpObjectStoreException(Throwable cause, String message) {
        super(cause, message);
    }

    public SpObjectStoreException(String format, Object... args) {
        super(format, args);
    }

    public SpObjectStoreException(String message) {
        super(message);
    }
}
