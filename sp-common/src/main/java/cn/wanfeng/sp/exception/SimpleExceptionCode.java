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

    FIREWORKS_ID_GENERATE_FAILED("00011", "烟花算法ID生成失败 %s"),

    AUTO_ADAPT_CREATE_MAPPING_NO_MATCH_CLASS("00012", "自动添加OpenSearch mapping失败，未找到适配的Class类型：%s"),

    OBJECT_ID_NOT_FOUND("00013", "对象id（%s）不存在"),

    OBJECT_PATH_EXIST_STORE_FAILED("00014", "路径已存在[%s]，保存到数据表[%s]失败"),

    OBJECT_PATH_NOT_FOUND("00015", "对象路径（%s）不存在"),

    FILE_STORAGE_BUCKET_NOT_FOUND("00016", "桶[%s]不存在, 请检查MinIO配置是否正确"),

    RANDOM_PASSWORD_LENGTH_LOWER_ZERO("00017", "生成随机密码的位数不能小于等于0 length: %d"),

    CLASS_NOT_SUPPORT_PROTO_SERIALIZE("00018", "类型[%s]不支持SimpleProto序列化"),

    LOCAL_FILE_NOT_EXIST("00019", "文件[%s]不存在"),

    FILE_NAME_FORMAT_INVALID("00020", "name[%s]不是一个文件名"),

    FIREWORKS_ID_GENERATE_FAILED_TYPE_OUT_OF_RANGE("00021", "烟花ID生成失败 type = {}, 不在合法范围 100~921 中"),
    FIREWORKS_ID_GENERATE_FAILED_START_TIME_GT_END("00022", "烟花ID生成失败 startTime = {}，开始时间不能大于等于当前时间"),
    FIREWORKS_ID_GENERATE_FAILED_OFFSET_GT_MAX("00023", "烟花ID生成失败 secondOffset = {}，时间偏移量超过最大值 9999999999"),



    /**
     * 未知异常，请根据日志排查原因
     */
    UNKNOWN_EXCEPTION("99999", "未知异常，请联系系统管理员");

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
