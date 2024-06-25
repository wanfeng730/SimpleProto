package cn.wanfeng.sp.proto.record;

import cn.wanfeng.sp.proto.type.ProtoType;

/**
 * @date: 2024-02-06 23:27
 * @author: luozh
 * @since: 1.0
 */
public class ProtoRecord {

    private final int indexNo;
    private final ProtoType type;
    private final int valueLen;
    private final int len;
    private final Object value;

    public ProtoRecord(int indexNo, ProtoType type, int valueLen, int len, Object value) {
        this.indexNo = indexNo;
        this.type = type;
        this.valueLen = valueLen;
        this.len = len;
        this.value = value;
    }

    public static ProtoRecordBuilder newBuilder() {
        return new ProtoRecordBuilder();
    }

    public int getIndexNo() {
        return indexNo;
    }

    public ProtoType getType() {
        return type;
    }

    public int getValueLen() {
        return valueLen;
    }

    public int getLen() {
        return len;
    }

    public Object getValue() {
        return value;
    }

    public boolean isStringType() {
        return type != null && ProtoType.STRING.equals(type);
    }

    public boolean isTextType() {
        return type != null && ProtoType.TEXT.equals(type);
    }

    public boolean isEmptyValue() {
        return value == null;
    }

    public boolean equalsIndexNo(int indexNo) {
        return this.indexNo == indexNo;
    }

    public boolean equals(ProtoRecord record) {
        return this.indexNo == record.getIndexNo() && this.type.equals(record.getType()) && this.value.equals(record.getValue());
    }
}
