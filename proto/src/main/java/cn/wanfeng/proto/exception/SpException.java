package cn.wanfeng.proto.exception;

/**
 * @date: 2024-06-24 10:33
 * @author: luozh
 * @description:
 * @since:
 */
public class SpException extends RuntimeException {

    public SpException(String message) {
        super(message);
    }

    public SpException(Throwable cause) {
        super(cause);
    }

    public SpException(String message, Throwable cause) {
        super(message, cause);
    }
}
