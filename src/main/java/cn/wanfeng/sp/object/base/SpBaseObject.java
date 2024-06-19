package cn.wanfeng.sp.object.base;

import cn.wanfeng.sp.proto.type.ProtoType;

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

    private static final String ID_COL = "id";
    private static final String TYPE_COL = "type";
    private static final String CREATE_DATE_COL = "create_date";
    private static final String MODIFY_DATE_COL = "modify_date";
    private static final String IS_DELETE_COL = "is_delete";
    private static final String DATA_COL = "data";

    private static final int ID_INDEX = 1;
    private static final int TYPE_INDEX = 2;
    private static final int CREATE_DATE_INDEX = 3;
    private static final int MODIFY_DATE_INDEX = 4;
    private static final int IS_DELETE_INDEX = 5;

    private static final ProtoType ID_PROTO_TYPE = ProtoType.LONG;
    private static final ProtoType TYPE_PROTO_TYPE = ProtoType.STRING;
    private static final ProtoType CREATE_DATE_PROTO_TYPE = ProtoType.DATE;
    private static final ProtoType MODIFY_DATE_PROTO_TYPE = ProtoType.DATE;
    private static final ProtoType IS_DELETE_PROTO_TYPE = ProtoType.BOOLEAN;

    public SpBaseObject(Long id) {
        //从数据库获取此id的字段

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
