package cn.wanfeng.sp.constants;

/**
 * @date: 2024-06-24 10:32
 * @author: luozh
 * @description:
 * @since:
 */
public class SpFailedReason {

    private static final String STRING_VALUE_LENGTH_TOO_LONG = "The Length of String Value (%s) is too Long Failed!";

    private static final String PROTO_FIELD_DUPLICATE_REASON_FORMAT = "Add @ProtoField[index=%d, name=%s] Verify Failed! Because Duplicate with @ProtoField[index=%d, name=%s]";

    public static String stringValueLengthTooLong(int valueLen) {
        return String.format(STRING_VALUE_LENGTH_TOO_LONG, valueLen);
    }

    public static String protoFieldDuplicate(int addIndexNo, String addName, int existIndexNo, String existName) {
        return String.format(PROTO_FIELD_DUPLICATE_REASON_FORMAT, addIndexNo, addName, existIndexNo, existName);
    }
}
