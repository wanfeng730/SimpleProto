package cn.wanfeng.sp.proto.record;

import cn.wanfeng.sp.proto.type.ProtoType;

/**
 * @date: 2024-02-07 22:22
 * @author: luozh
 * @since: 1.0
 */
public class ProtoRecordBuilder {
    private int indexNo;
    private ProtoType type;
    private int valueLen;
    private int len;
    private Object value;

    public ProtoRecordBuilder indexNo(int indexNo) {
        this.indexNo = indexNo;
        return this;
    }

    public ProtoRecordBuilder type(ProtoType type) {
        this.type = type;
        return this;
    }

    public ProtoRecordBuilder valueLen(int valueLen) {
        this.valueLen = valueLen;
        return this;
    }

    public ProtoRecordBuilder len(int len) {
        this.len = len;
        return this;
    }

    public ProtoRecordBuilder value(Object value) {
        this.value = value;
        return this;
    }

    public ProtoRecord build() {
        return new ProtoRecord(indexNo, type, valueLen, len, value);
    }

}
