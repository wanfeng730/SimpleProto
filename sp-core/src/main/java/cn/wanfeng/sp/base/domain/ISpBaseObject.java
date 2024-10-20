package cn.wanfeng.sp.base.domain;

import cn.wanfeng.proto.type.ProtoType;

/**
 * @date: 2024-04-02 23:48
 * @author: luozh
 * @since: 1.0
 */
public interface ISpBaseObject {
    String ID_COL = "id";
    String TYPE_COL = "type";
    String NAME_COL = "name";
    String CREATE_DATE_COL = "create_date";
    String MODIFY_DATE_COL = "modify_date";
    String IS_DELETE_COL = "is_delete";
    String DATA_COL = "data";

    int ID_INDEX = 1001;
    int TYPE_INDEX = 1002;
    int NAME_INDEX = 1003;
    int CREATE_DATE_INDEX = 1004;
    int MODIFY_DATE_INDEX = 1005;
    int IS_DELETE_INDEX = 1006;

    ProtoType ID_PROTO_TYPE = ProtoType.LONG;
    ProtoType NAME_PROTO_TYPE = ProtoType.STRING;
    ProtoType TYPE_PROTO_TYPE = ProtoType.STRING;
    ProtoType CREATE_DATE_PROTO_TYPE = ProtoType.DATE;
    ProtoType MODIFY_DATE_PROTO_TYPE = ProtoType.DATE;
    ProtoType IS_DELETE_PROTO_TYPE = ProtoType.BOOLEAN;

    String BASE_OBJECT_ID_INCREASE_NAME = "SpBaseObjectId";

    int EMPTY_OBJECT_VALUE = -999;

    /**
     * 将对象持久化
     */
    void store();

    /**
     * 将对象从持久化删除
     */
    void remove();
}
