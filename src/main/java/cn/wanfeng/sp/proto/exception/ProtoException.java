package cn.wanfeng.sp.proto.exception;

/**
 * @date: 2024-02-08 15:33
 * @author: luozh
 * @description:
 * @since:
 */
public class ProtoException extends RuntimeException {

    public ProtoException(String message) {
        super(message);
    }

    public ProtoException(Throwable cause) {
        super(cause);
    }

    public ProtoException(String message, Throwable cause) {
        super(message, cause);
    }
}


