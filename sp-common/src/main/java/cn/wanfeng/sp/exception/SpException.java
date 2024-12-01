package cn.wanfeng.sp.exception;

/**
 * @date: 2024-06-24 10:33
 * @author: luozh
 */
public class SpException extends RuntimeException {

    public SpException(String message) {
        super(message);
    }

    public SpException(String format, Object... args){
        super(String.format(format, args));
    }

    public SpException(Throwable cause) {
        super(cause);
    }

    public SpException(String message, Throwable cause) {
        super(message, cause);
    }
}
