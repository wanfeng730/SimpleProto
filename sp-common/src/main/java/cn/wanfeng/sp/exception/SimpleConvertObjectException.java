package cn.wanfeng.sp.exception;

/**
 * @date: 2024-12-01 15:50
 * @author: luozh.wanfeng
 * @description: 类型转换异常
 * @since: 1.0
 */
public class SimpleConvertObjectException extends RuntimeException{

    public SimpleConvertObjectException(String message) {
        super(message);
    }

    public SimpleConvertObjectException(String format, Object... args){
        super(String.format(format, args));
    }

    public SimpleConvertObjectException(Throwable cause) {
        super(cause);
    }

    public SimpleConvertObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
