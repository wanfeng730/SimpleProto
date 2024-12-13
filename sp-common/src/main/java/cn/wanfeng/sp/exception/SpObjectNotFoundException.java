package cn.wanfeng.sp.exception;


/**
 * @date: 2024-12-14 00:13
 * @author: luozh.wanfeng
 * @description: 
 * @since: 
 */
public class SpObjectNotFoundException extends SpException {

    public SpObjectNotFoundException(Throwable cause) {
        super(cause);
    }

    public SpObjectNotFoundException(String format, Object... args) {
        super(format, args);
    }

    public SpObjectNotFoundException(String message) {
        super(message);
    }

    public SpObjectNotFoundException(String message, Throwable cause) {
        super(cause, message);
    }

    public SpObjectNotFoundException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }
}
