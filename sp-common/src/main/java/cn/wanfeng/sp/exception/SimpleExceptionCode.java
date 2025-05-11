package cn.wanfeng.sp.exception;

/**
 * ExceptionCode: SimpleProto异常编码.
 *
 * @date: 2025-05-01 13:50
 * @author: luozh.wanfeng
 */
public enum SimpleExceptionCode implements ExceptionInfoGetter {


    /**
     * 测试
     */
    TEST("00000", "TEST_MESSAGE_CODE"),
    /**
     * 测试携带参数：%s
     */
    TEST_HAS_ARGS("00001", "TEST_MESSAGE_HAS_ARGS"),
    /**
     * 批量保存暂不支持文件类型的对象，请检查
     */
    BULK_STORE_NOT_SUPPORT_FILE_OBJECT("00002", "BULK_STORE_NOT_SUPPORT_FILE_OBJECT"),
    /**
     * 未知异常，请根据日志排查原因
     */
    UNKNOWN_EXCEPTION("99999", "UNKNOWN_EXCEPTION");

    private final String code;

    private final String message;

    SimpleExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
