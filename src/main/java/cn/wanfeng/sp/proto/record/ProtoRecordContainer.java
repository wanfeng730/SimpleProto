package cn.wanfeng.sp.proto.record;

import java.util.ArrayList;
import java.util.List;

/**
 * @date: 2024-02-16 16:54
 * @author: luozh
 * @since: 1.0
 */
public class ProtoRecordContainer {

    private List<ProtoRecord> recordList;
    private int totalSize;

    private ProtoRecordContainer() {
    }

    public static ProtoRecordContainer emptyContainer() {
        ProtoRecordContainer container = new ProtoRecordContainer();
        container.setRecordList(new ArrayList<>());
        return container;
    }

    public List<ProtoRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<ProtoRecord> recordList) {
        this.recordList = recordList;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public void addRecord(ProtoRecord record) {
        this.recordList.add(record);
        this.totalSize += record.getLen();
    }

    public void addRecords(List<ProtoRecord> records) {
        this.recordList.addAll(records);
        records.forEach(record -> this.totalSize += record.getLen());
    }

    public boolean isEmpty() {
        return recordList == null || recordList.isEmpty();
    }


}
