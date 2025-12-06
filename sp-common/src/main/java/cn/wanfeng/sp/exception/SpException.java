package cn.wanfeng.sp.exception;

import java.io.Serial;

/**
 * @date: 2024-06-24 10:33
 * @author: luozh
 */
public class SpException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 202505020019L;

    private String code;

    private String message;

    private Object[] args;

    public SpException(String message) {
        super(message);
        this.message = message;
    }

    public SpException(Throwable cause) {
        super(cause);
        this.code = SimpleExceptionCode.UNKNOWN_EXCEPTION.getCode();
        this.message = SimpleExceptionCode.UNKNOWN_EXCEPTION.getMessage();
    }

    public SpException(Throwable cause, String message) {
        super(message, cause);
        this.code = SimpleExceptionCode.UNKNOWN_EXCEPTION.getCode();
        this.message = message;
    }

    public SpException(Throwable cause, String format, Object... args){
        super(String.format(format, args), cause);
        this.code = SimpleExceptionCode.UNKNOWN_EXCEPTION.getCode();
        this.message = String.format(format, args);
    }

    public SpException(String code, String message, Object[] args) {
        this.code = code;
        this.message = String.format(message, args);
        this.args = args;
    }

    public SpException(ExceptionInfoGetter exceptionInfoGetter) {
        this.code = exceptionInfoGetter.getCode();
        this.message = exceptionInfoGetter.getMessage();
    }

    public SpException(ExceptionInfoGetter exceptionInfoGetter, Object... args) {
        this.code = exceptionInfoGetter.getCode();
        this.message = String.format(exceptionInfoGetter.getMessage(), args);
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }
}
