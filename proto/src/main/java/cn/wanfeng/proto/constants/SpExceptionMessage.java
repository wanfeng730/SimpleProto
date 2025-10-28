package cn.wanfeng.proto.constants;

/**
 * @date: 2024-06-24 10:32
 * @author: luozh
 * @description: generate exception message in sp-core
 * @since: 1.0
 */
public class SpExceptionMessage {

    public static final String OBJECT_ID_IS_NULL_WHEN_FIND_OBJECT = "Object Id is NULL when find object from database";

    private static final String STRING_VALUE_LENGTH_TOO_LONG = "The Length of String Value (%s) is too Long Failed!";

    private static final String PROTO_FIELD_INDEX_DUPLICATE = "@ProtoField[index=%d, name=%s] Verify Failed! Because Duplicate with index, please confirm index and name all not duplicate！";

    private static final String PROTO_FIELD_NAME_DUPLICATE = "@ProtoField[index=%d, name=%s] Verify Failed! Because Duplicate with name, please confirm index and name all not duplicate！";

    private static final String OBJECT_TYPE_NOT_EQUALS_DEFINE = "对象[id=%s]的类型为[%s]，与定义的类型[%s]不同";

    public static String stringValueLengthTooLong(int valueLen) {
        return String.format(STRING_VALUE_LENGTH_TOO_LONG, valueLen);
    }

    public static String protoFieldIndexDuplicate(int addIndexNo, String addName) {
        return String.format(PROTO_FIELD_INDEX_DUPLICATE, addIndexNo, addName);
    }

    public static String protoFieldNameDuplicate(int addIndex, String addName) {
        return String.format(PROTO_FIELD_NAME_DUPLICATE, addIndex, addName);
    }

    public static String objectTypeNotEqualsDefine(Long id, String actualType, String defineType) {
        return String.format(OBJECT_TYPE_NOT_EQUALS_DEFINE, id, actualType, defineType);
    }



    public static final String AUTO_CREATE_MAPPING_ERROR = "自动创建Mapping出现未知异常";
}
