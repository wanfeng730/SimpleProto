package cn.wanfeng.sp.base.object;

import cn.wanfeng.sp.base.anno.ProtoField;
import cn.wanfeng.sp.constants.SpFailedReason;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.proto.record.ProtoRecord;
import cn.wanfeng.sp.proto.record.ProtoRecordContainer;
import cn.wanfeng.sp.proto.record.ProtoRecordFactory;
import cn.wanfeng.sp.util.LogUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date: 2024-04-02 23:34
 * @author: luozh
 * @description: simpleproto基础对象
 * @since: 1.0
 */
public class SpBaseObject implements ISpBaseObject {

    private Class<?> id;

    private String type;

    private String name;

    private Date createDate;

    private Date modifyDate;

    private Boolean isDelete;

    private static ConcurrentHashMap<Integer, Object> DATA_MAP;

    public SpBaseObject(SpSession session, Long id) {
        //从数据库获取此id的字段
        SpBaseObjectDO objectDO = session.baseObjectStorage().findById(id);
        ProtoRecordContainer container = ProtoRecordFactory.readBytesToRecordList(objectDO.getData());
        LogUtils.debug(container.getRecordList().toString());

        //设置属性值
        initDataMap();
        //校验ProtoField注解定义的index和name是否重复
        verifyProtoFieldDuplicate();
        //读取container中的数据并设置到本对象的属性中
        readContainerToFields(container);

        //将businessRecords设置到子类对应index的属性中
    }

    @Override
    public void store() {

    }

    @Override
    public void remove() {

    }

    private static void initDataMap() {
        DATA_MAP = new ConcurrentHashMap<>(16);
    }

    private void verifyProtoFieldDuplicate() {
        Map<Integer, ProtoField> existIndexMap = new HashMap<>();
        Map<String, ProtoField> existNameMap = new HashMap<>();
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(ProtoField.class)) {
                assertIndexNotDuplicate(existIndexMap, declaredField.getAnnotation(ProtoField.class));
                assertNameNotDuplicate(existNameMap, declaredField.getAnnotation(ProtoField.class));
            }
        }
    }

    private static void assertNotDuplicateWithBaseField(ProtoField protoField) {
        // LzhTODO: 校验业务对象的字段index和name是否与基础对象的字段重复
    }

    private static void assertIndexNotDuplicate(Map<Integer, ProtoField> existIndexMap, ProtoField protoField) {
        int indexNo = protoField.index();
        String name = protoField.name();
        if (existIndexMap.containsKey(indexNo)) {
            throw new SpException(SpFailedReason.protoFieldDuplicate(indexNo, name, existIndexMap.get(indexNo).index(), existIndexMap.get(indexNo).name()));
        }
        existIndexMap.put(indexNo, protoField);
    }

    private static void assertNameNotDuplicate(Map<String, ProtoField> existNameMap, ProtoField protoField) {
        int indexNo = protoField.index();
        String name = protoField.name();
        if (existNameMap.containsKey(name)) {
            throw new SpException(SpFailedReason.protoFieldDuplicate(indexNo, name, existNameMap.get(name).index(), existNameMap.get(name).name()));
        }
        existNameMap.put(name, protoField);
    }


    public void readContainerToFields(ProtoRecordContainer container) {
        for (ProtoRecord record : container.getRecordList()) {
            switch (record.getIndexNo()) {
                //基础对象字段
                case ID_INDEX -> {
                    id = (Class<?>) record.getValue();
                    DATA_MAP.put(ID_INDEX, id);
                }
                case TYPE_INDEX -> {
                    type = (String) record.getValue();
                    DATA_MAP.put(TYPE_INDEX, type);
                }
                case NAME_INDEX -> {
                    name = (String) record.getValue();
                    DATA_MAP.put(NAME_INDEX, name);
                }
                case CREATE_DATE_INDEX -> {
                    createDate = (Date) record.getValue();
                    DATA_MAP.put(CREATE_DATE_INDEX, createDate);
                }
                case MODIFY_DATE_INDEX -> {
                    modifyDate = (Date) record.getValue();
                    DATA_MAP.put(MODIFY_DATE_INDEX, modifyDate);
                }
                case IS_DELETE_INDEX -> {
                    isDelete = (Boolean) record.getValue();
                    DATA_MAP.put(IS_DELETE_INDEX, isDelete);
                }
                //业务对象字段
                default -> readRecordToBusinessField(record);
            }
        }
    }

    public void readRecordToBusinessField(ProtoRecord record) {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ProtoField.class)) {
                int indexNo = field.getAnnotation(ProtoField.class).index();
                if (record.getIndexNo() != indexNo) {
                    continue;
                }
                try {
                    field.set(this, record.getValue());
                    DATA_MAP.put(indexNo, record.getValue());
                    return;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
