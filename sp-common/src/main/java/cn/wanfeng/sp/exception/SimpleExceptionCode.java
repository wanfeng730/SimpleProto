package cn.wanfeng.sp.exception;

/**
 * ExceptionCode: SimpleProto异常编码.
 *
 * @date: 2025-05-01 13:50
 * @author: luozh.wanfeng
 */
public enum SimpleExceptionCode implements ExceptionInfoGetter {


    TEST("00000", "测试"),

    TEST_HAS_ARGS("00001", "测试携带参数：%s"),

    BULK_STORE_NOT_SUPPORT_FILE_OBJECT("00002", "批量保存暂不支持文件类型的对象，请检查"),


    QUERY_MODEL_TO_WRAPPER_HAS_BLANK_FIELD_NAME("00003", "查询参数中不允许存在空的字段名，请检查"),

    QUERY_MODEL_TO_WRAPPER_OPERATOR_ERROR("00004", "查询参数中查询字段[%s]的操作符不为可选项（1~16），请检查"),

    QUERY_MODEL_TO_WRAPPER_FIELD_TYPE_ERROR("00005", "查询参数中查询字段[%s]的字段类型不为可选项，请检查"),

    QUERY_MODEL_TO_WRAPPER_FIELD_VALUE_CONVERT_FAILED("00006", "查询参数中查询字段[%s]，无法将value[%s]转换为[%s]类型，请检查"),

    QUERY_MODEL_TO_WRAPPER_FIELD_VALUE_BLANK("00007", "查询参数中查询字段[%s]的值不能为空，请检查"),

    QUERY_MODEL_TO_WRAPPER_VALUE_NO_BETWEEN_SEPARATOR("00008", "查询参数中查询字段[%s]的值需要有范围分隔符[~]，请检查"),

    QUERY_MODEL_TO_WRAPPER_VALUE_NO_IN_SEPARATOR("00009", "查询参数中查询字段[%s]的值需要有可选项分隔符[,]，请检查"),

    QUERY_MODEL_TO_WRAPPER_SORT_TYPE_ERROR("00010", "查询参数中查询字段[%s]的排序方式不为可选项，请检查"),




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
