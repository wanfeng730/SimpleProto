package cn.wanfeng.sp.util;

import cn.wanfeng.proto.record.ProtoRecordContainer;
import cn.wanfeng.proto.record.ProtoRecordFactory;
import cn.wanfeng.sp.base.object.ISpBaseObject;
import cn.wanfeng.sp.base.object.SpBaseObjectDO;

/**
 * @date: 2024-06-21 13:59
 * @author: luozh
 * @description:
 * @since:
 */
public class TestUtils {

    public static ProtoRecordContainer testBaseObjectProtoRecord(SpBaseObjectDO objectDO) {
        ProtoRecordContainer container = ProtoRecordContainer.emptyContainer();
        container.addRecord(ProtoRecordFactory.buildProtoRecordByIndexAndValue(ISpBaseObject.ID_INDEX, objectDO.getId()));
        container.addRecord(ProtoRecordFactory.buildProtoRecordByIndexAndValue(ISpBaseObject.TYPE_INDEX, objectDO.getType()));
        container.addRecord(ProtoRecordFactory.buildProtoRecordByIndexAndValue(ISpBaseObject.NAME_INDEX, objectDO.getName()));
        container.addRecord(ProtoRecordFactory.buildProtoRecordByIndexAndValue(ISpBaseObject.CREATE_DATE_INDEX, objectDO.getCreateDate()));
        container.addRecord(ProtoRecordFactory.buildProtoRecordByIndexAndValue(ISpBaseObject.MODIFY_DATE_INDEX, objectDO.getModifyDate()));
        container.addRecord(ProtoRecordFactory.buildProtoRecordByIndexAndValue(ISpBaseObject.IS_DELETE_INDEX, objectDO.getIsDelete()));
        return container;
    }

}
