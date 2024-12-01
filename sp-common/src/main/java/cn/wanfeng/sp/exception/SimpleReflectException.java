package cn.wanfeng.sp.exception;

/**
 * @date: 2024-12-01 15:19
 * @author: luozh.wanfeng
 */
public class SimpleReflectException extends RuntimeException{

    public SimpleReflectException(String message) {
        super(message);
    }

    public SimpleReflectException(String format, Object... args){
        super(String.format(format, args));
    }

    public SimpleReflectException(Throwable cause) {
        super(cause);
    }

    public SimpleReflectException(String message, Throwable cause) {
        super(message, cause);
    }

}
