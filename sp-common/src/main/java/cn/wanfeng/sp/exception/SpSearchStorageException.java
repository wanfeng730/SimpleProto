package cn.wanfeng.sp.exception;


/**
 * @date: 2024-12-23 23:42
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public class SpSearchStorageException extends SpException{

    public SpSearchStorageException(Throwable cause, String message) {
        super(cause, message);
    }

    public SpSearchStorageException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }
}
