package cn.wanfeng.sp.base.object;

import cn.wanfeng.sp.proto.record.ProtoRecordContainer;
import cn.wanfeng.sp.proto.record.ProtoRecordFactory;
import cn.wanfeng.sp.util.LogUtils;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @date: 2024-04-02 23:34
 * @author: luozh
 * @description: simpleproto基础对象
 * @since: 1.0
 */
public class SpBaseObject implements ISpBaseObject {

    private Long id;

    private String type;

    private Date createDate;

    private Date modifyDate;

    private Boolean isDelete;

    private LinkedHashMap<Integer, Object> businessRecords;

    public SpBaseObject(SpSession session, Long id) {
        //从数据库获取此id的字段
        SpBaseObjectDO objectDO = session.baseObjectStorage().findById(id);
        ProtoRecordContainer container = ProtoRecordFactory.readBytesToRecordList(objectDO.getData());
        LogUtils.info(container.getRecordList().toString());

        //序列化data字段到businessRecords中

        //将businessRecords设置到子类对应index的属性中
    }

    @Override
    public void store() {

    }

    @Override
    public void remove() {

    }


}
