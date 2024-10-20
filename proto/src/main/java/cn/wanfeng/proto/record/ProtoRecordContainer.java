package cn.wanfeng.proto.record;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @date: 2024-02-16 16:54
 * @author: luozh
 * @since: 1.0
 */
public class ProtoRecordContainer {

    private ConcurrentHashMap<Integer, ProtoRecord> indexNoRecordMap;

    private int totalSize;

    private ProtoRecordContainer() {
    }

    public static ProtoRecordContainer emptyContainer() {
        ProtoRecordContainer container = new ProtoRecordContainer();
        container.setIndexNoRecordMap(new ConcurrentHashMap<>(16));
        container.setTotalSize(0);
        return container;
    }

    public void putRecord(ProtoRecord protoRecord) {
        int indexNo = protoRecord.getIndexNo();
        if (indexNoRecordMap.containsKey(indexNo)) {
            removeRecord(indexNo);
        }
        this.indexNoRecordMap.put(protoRecord.getIndexNo(), protoRecord);
        this.totalSize += protoRecord.getLen();
    }

    public void removeRecord(Integer indexNo) {
        ProtoRecord oldRecord = this.indexNoRecordMap.get(indexNo);
        this.indexNoRecordMap.remove(oldRecord.getIndexNo());
        this.totalSize -= oldRecord.getLen();
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public ConcurrentHashMap<Integer, ProtoRecord> getIndexNoRecordMap() {
        return indexNoRecordMap;
    }

    public void setIndexNoRecordMap(ConcurrentHashMap<Integer, ProtoRecord> indexNoRecordMap) {
        this.indexNoRecordMap = indexNoRecordMap;
    }

    public boolean isEmpty() {
        return indexNoRecordMap.isEmpty();
    }
}
