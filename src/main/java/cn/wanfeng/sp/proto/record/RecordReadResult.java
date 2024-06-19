package cn.wanfeng.sp.proto.record;

/**
 * @date: 2024-02-10 10:58
 * @author: luozh
 * @since: 1.0
 */
public class RecordReadResult {
    private final byte[] nextData;
    private final boolean success;
    private final ProtoRecord record;

    public RecordReadResult(byte[] nextData, boolean success, ProtoRecord record) {
        this.nextData = nextData;
        this.success = success;
        this.record = record;
    }

    public static RecordReadResult buildSuccessResult(byte[] nextData, ProtoRecord record) {
        return new RecordReadResult(nextData, true, record);
    }

    public static RecordReadResult buildFailedResult() {
        return new RecordReadResult(null, false, null);
    }

    public byte[] getNextData() {
        return nextData;
    }

    public boolean isSuccess() {
        return success;
    }

    public ProtoRecord getRecord() {
        return record;
    }
}
