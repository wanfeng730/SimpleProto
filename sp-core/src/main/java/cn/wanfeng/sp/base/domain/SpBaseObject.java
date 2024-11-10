package cn.wanfeng.sp.base.domain;

import cn.wanfeng.proto.constants.SpExceptionMessage;
import cn.wanfeng.proto.exception.SpException;
import cn.wanfeng.proto.record.ProtoRecord;
import cn.wanfeng.proto.record.ProtoRecordContainer;
import cn.wanfeng.proto.record.ProtoRecordFactory;
import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.anno.Type;
import cn.wanfeng.sp.base.object.SpBaseObjectDO;
import cn.wanfeng.sp.base.object.SpSettingsDO;
import cn.wanfeng.sp.config.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.SpObjectConvertUtils;
import cn.wanfeng.sp.util.SpReflectUtils;
import com.github.f4b6a3.ulid.UlidCreator;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date: 2024-04-02 23:34
 * @author: luozh
 * @description: simpleproto基础对象
 * @since: 1.0
 */
public class SpBaseObject implements ISpBaseObject {

    protected SpSession spSession;

    protected Long id;

    protected String type;

    protected String name;

    protected Date createDate;

    protected Date modifyDate;

    protected Boolean isDelete;

    protected ProtoRecordContainer recordContainer;

    protected ConcurrentHashMap<String, Object> fieldNameValueMap;

    /**
     * 该对象是否为新建的对象（该id未在数据库中存在）
     */
    protected boolean isNewObject;

    public SpBaseObject(SpSession session, String type) {
        this(session, type, defaultName());
    }

    public SpBaseObject(SpSession session, String type, String name) {
        this.spSession = session;
        this.isNewObject = true;
        initBaseObject(type, name);
        initRecordContainerAndValueMap();
    }

    public SpBaseObject(SpSession session, Long id) {
        this.spSession = session;
        this.isNewObject = false;

        assertObjectIdNotNull(id);
        // 从数据库获取此id的字段
        SpBaseObjectDO objectDO = spSession.databaseStorage().findObjectById(SimpleProtoConfig.dataTable, id);
        assertObjectIdFindFromDatabase(id, objectDO);

        ProtoRecordContainer container = ProtoRecordFactory.readBytesToRecordList(objectDO.getData());
        LogUtils.debug(container.getIndexNoRecordMap().toString());

        // 校验ProtoField注解定义的index和name是否重复
        verifyAndSetRecordMap(container);
        // 读取container中的数据并设置到本对象的属性中
        readRecordMap();
    }

    private static void assertObjectIdNotNull(Long id) {
        if (Objects.isNull(id)) {
            throw new SpException(SpExceptionMessage.OBJECT_ID_IS_NULL_WHEN_FIND_OBJECT);
        }
    }

    private static void assertObjectIdFindFromDatabase(Long id, SpBaseObjectDO spBaseObjectDO) {
        if (Objects.isNull(spBaseObjectDO)) {
            throw new SpException(SpExceptionMessage.objectIdNotFound(id));
        }
    }

    private void verifyAndSetRecordMap(ProtoRecordContainer container) {
        this.recordContainer = container;
        this.fieldNameValueMap = new ConcurrentHashMap<>(16);

        ConcurrentHashMap<Integer, ProtoRecord> indexNoRecordMap = container.getIndexNoRecordMap();

        Set<Integer> existIndexSet = new HashSet<>();
        Set<String> existFieldNameSet = new HashSet<>();

        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (!declaredField.isAnnotationPresent(ProtoField.class)) {
                continue;
            }
            ProtoField protoField = declaredField.getAnnotation(ProtoField.class);
            assertIndexAndNameNotDuplicate(existIndexSet, existFieldNameSet, protoField);
            int annoIndexNo = protoField.index();
            String annoFieldName = protoField.name();
            if (!indexNoRecordMap.containsKey(annoIndexNo) || StringUtils.isBlank(annoFieldName)) {
                continue;
            }
            ProtoRecord protoRecord = indexNoRecordMap.get(annoIndexNo);
            fieldNameValueMap.put(annoFieldName, protoRecord);
        }
    }

    protected void readRecordMap() {
        // 读取并设置该对象的基本属性
        readRecordMapToBaseObjectProperty();
        // 读取并设置该对象继承类的属性
        readRecordMapToAnnotationProperty();
    }

    /**
     * 将protoRecord设置到对应的SpBaseObject的成员属性上
     */
    protected void readRecordMapToBaseObjectProperty() {
        ConcurrentHashMap<Integer, ProtoRecord> indexNoRecordMap = recordContainer.getIndexNoRecordMap();
        id = (Long) indexNoRecordMap.get(ID_INDEX).getValue();
        type = (String) indexNoRecordMap.get(TYPE_INDEX).getValue();
        name = (String) indexNoRecordMap.get(NAME_INDEX).getValue();
        createDate = (Date) indexNoRecordMap.get(CREATE_DATE_INDEX).getValue();
        modifyDate = (Date) indexNoRecordMap.get(MODIFY_DATE_INDEX).getValue();
        isDelete = (Boolean) indexNoRecordMap.get(IS_DELETE_INDEX).getValue();
    }

    protected void readRecordMapToAnnotationProperty() {
        ConcurrentHashMap<Integer, ProtoRecord> indexNoRecordMap = recordContainer.getIndexNoRecordMap();
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (!declaredField.isAnnotationPresent(ProtoField.class)) {
                continue;
            }
            int indexNo = declaredField.getAnnotation(ProtoField.class).index();
            String fieldName = declaredField.getAnnotation(ProtoField.class).name();
            if (!indexNoRecordMap.containsKey(indexNo)) {
                continue;
            }
            try {
                Object value = indexNoRecordMap.get(indexNo).getValue();
                declaredField.setAccessible(true);
                declaredField.set(this, value);
                // TEST_LOG
                LogUtils.debug("Set DeclaredField Property: {}={}", declaredField.getName(), declaredField.get(this));
            } catch (Exception e) {
                LogUtils.error(SpExceptionMessage.setPropertyNoAccessible(indexNo, fieldName));
                throw new SpException(e);
            }
        }
    }

    private static void assertIndexAndNameNotDuplicate(Set<Integer> existIndexSet, Set<String> existFieldNameSet, ProtoField protoField) {
        int index = protoField.index();
        String name = protoField.name();
        if (existIndexSet.contains(index)) {
            throw new SpException(SpExceptionMessage.protoFieldIndexDuplicate(index, name));
        }
        if (existFieldNameSet.contains(name)) {
            throw new SpException(SpExceptionMessage.protoFieldNameDuplicate(index, name));
        }
    }

    /**
     * 初始化基础对象的属性
     *
     * @param type 类型
     * @param name 名称
     */
    private void initBaseObject(String type, String name) {
        this.type = type;
        this.name = name;

        Date currentDate = new Date();
        this.createDate = currentDate;
        this.modifyDate = currentDate;
        this.isDelete = false;
    }

    /**
     * 初始化map，将基础对象的属性放入Container和fieldNameValueMap
     */
    private void initRecordContainerAndValueMap() {
        this.recordContainer = ProtoRecordContainer.emptyContainer();
        this.fieldNameValueMap = new ConcurrentHashMap<>();

        ProtoRecord typeRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(TYPE_INDEX, type);
        ProtoRecord nameRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(NAME_INDEX, name);
        ProtoRecord createDateRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(CREATE_DATE_INDEX, createDate);
        ProtoRecord modifyDateRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(MODIFY_DATE_INDEX, modifyDate);
        ProtoRecord isDeleteRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(IS_DELETE_INDEX, isDelete);

        recordContainer.putRecord(typeRecord);
        recordContainer.putRecord(nameRecord);
        recordContainer.putRecord(createDateRecord);
        recordContainer.putRecord(modifyDateRecord);
        recordContainer.putRecord(isDeleteRecord);

        fieldNameValueMap.put(TYPE_COL, typeRecord.getValue());
        fieldNameValueMap.put(NAME_COL, nameRecord.getValue());
        fieldNameValueMap.put(CREATE_DATE_COL, createDateRecord.getValue());
        fieldNameValueMap.put(MODIFY_DATE_COL, modifyDateRecord.getValue());
        fieldNameValueMap.put(IS_DELETE_COL, isDeleteRecord.getValue());
    }

    /**
     * 生成默认的对象名称
     *
     * @return ULID
     */
    private static String defaultName() {
        return UlidCreator.getUlid().toString();
    }

    @Override
    public void store() {
        assertTypeValueEqualsAnnotation();
        //根据是否为新对象执行store操作
        if (isNewObject) {
            // 生成主键id
            generateIncreaseId();
            // 将继承类中的属性放到indexNoRecordMap和fieldNameValueMap
            putThisPropertyToContainerAndValueMap();
            putDeclaredPropertyToContainerAndValueMap();
            // 新建数据到数据库（事务：数据库存储新建、设置自增主键id、高级搜索存储新建）
            createObjectToStorage();
        } else {
            // 修改时间刷新
            updateModifyDate();
            // 将继承类中的属性放到indexNoRecordMap和fieldNameValueMap
            putThisPropertyToContainerAndValueMap();
            putDeclaredPropertyToContainerAndValueMap();
            // 更新数据(事务：数据库存储更新、高级搜索存储更新）
            updateObjectToStorage();
        }

    }

    public void assertTypeValueEqualsAnnotation(){
        Type typeAnnotation = this.getClass().getAnnotation(Type.class);
        String typeAnnoValue = typeAnnotation.value();
        if(!StringUtils.equals(this.type, typeAnnoValue)){
            throw new SpException(String.format("保存对象异常，type的值必须和注解@Type标注的值[%s]一致，当前type=[%s]", typeAnnoValue, this.type));
        }
    }

    private void updateModifyDate() {
        this.modifyDate = new Date();
    }

    private void putThisPropertyToContainerAndValueMap(){
        //放入Container
        ProtoRecord idRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(ID_INDEX, this.id);
        recordContainer.putRecord(idRecord);
        ProtoRecord nameRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(NAME_INDEX, this.name);
        recordContainer.putRecord(nameRecord);
        ProtoRecord typeRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(TYPE_INDEX, this.type);
        recordContainer.putRecord(typeRecord);
        ProtoRecord createDateRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(CREATE_DATE_INDEX, this.createDate);
        recordContainer.putRecord(createDateRecord);
        ProtoRecord modifyDateRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(MODIFY_DATE_INDEX, this.modifyDate);
        recordContainer.putRecord(modifyDateRecord);
        ProtoRecord isDeleteRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(IS_DELETE_INDEX, this.isDelete);
        recordContainer.putRecord(isDeleteRecord);

        //放入fieldNameValueMap
        fieldNameValueMap.put(ID_COL, this.id);
        fieldNameValueMap.put(NAME_COL, this.name);
        fieldNameValueMap.put(TYPE_COL, this.type);
        fieldNameValueMap.put(CREATE_DATE_COL, this.createDate);
        fieldNameValueMap.put(MODIFY_DATE_COL, this.modifyDate);
        fieldNameValueMap.put(IS_DELETE_COL, this.isDelete);
    }

    private void putDeclaredPropertyToContainerAndValueMap(){
        Field[] fields = SpReflectUtils.getProtoFieldAnnotationFields(this.getClass());

        for (Field field : fields) {
            field.setAccessible(true);
            ProtoField protoField = field.getAnnotation(ProtoField.class);
            int indexNo = protoField.index();
            String fieldName = protoField.name();
            try {
                Object value = field.get(this);

                ProtoRecord record = ProtoRecordFactory.buildProtoRecordByIndexAndValue(indexNo, value);
                recordContainer.putRecord(record);

                fieldNameValueMap.put(fieldName, value);
            } catch (IllegalAccessException e) {
                LogUtils.error(SpExceptionMessage.setPropertyNoAccessible(indexNo, fieldName));
                throw new SpException(e);
            }
        }
    }

    private void createObjectToStorage(){
        // 序列化，构造对象数据保存到数据库
        byte[] data = ProtoRecordFactory.writeRecordListToBytes(recordContainer);
        SpBaseObjectDO baseObjectDO = SpObjectConvertUtils.convertSpBaseObjectToDO(this, data);
        //获取id自增锁

        try {
            spSession.createObjectToStorage(baseObjectDO, fieldNameValueMap);
        } catch (Exception e) {
            LogUtils.error("对象创建失败，数据已回滚，失败原因", e);
        } finally {

        }
    }

    private void updateObjectToStorage(){
        // 所有字段序列化成字节数组
        byte[] data = ProtoRecordFactory.writeRecordListToBytes(recordContainer);
        // 该对象更新到数据库
        SpBaseObjectDO baseObjectDO = SpObjectConvertUtils.convertSpBaseObjectToDO(this, data);
        try {
            spSession.updateObjectToStorage(baseObjectDO, fieldNameValueMap);
        } catch (Exception e) {
            LogUtils.error("对象更新失败，数据已回滚，失败原因", e);
        }
    }

    private void generateIncreaseId() {
        boolean locked = spSession.cacheOperator().lock(OBJECT_ID_INCREASE_NAME);
        if(locked){
            try {
                SpSettingsDO idIncreaseDO = spSession.databaseStorage().findSettingsByName(SimpleProtoConfig.settingsTable, OBJECT_ID_INCREASE_NAME);
                this.id = Objects.isNull(idIncreaseDO) ? 1L : idIncreaseDO.getIncreaseLong() + 1;
                // 自增后的id保存到数据库
                SpSettingsDO settingsDO = new SpSettingsDO();
                settingsDO.setName(OBJECT_ID_INCREASE_NAME);
                settingsDO.setIncreaseLong(this.id);
                //更新设置表
                spSession.databaseStorage().updateSettings(SimpleProtoConfig.settingsTable, settingsDO);
            } catch (Exception e) {
                throw new SpException("生成自增id时出现未知异常", e);
            } finally {
                spSession.cacheOperator().unLock(OBJECT_ID_INCREASE_NAME);
                LogUtils.info("释放锁");
            }
        }
    }


    @Override
    public void remove() {
        if (isNewObject) {
            LogUtils.info("对象[id={}, type={}, name={}]为新建对象，未保存到数据库，无需删除", this.id, this.type, this.name);
        } else {
            // 在数据库中删除
            removeObjectFromStorage();
        }
    }

    private void removeObjectFromStorage(){
        try {
            spSession.removeObjectFromStorage(this.id);
        } catch (Exception e) {
            LogUtils.error("对象删除失败，数据已回滚，失败原因", e);
        }
    }


    ////////////////////////// getter and setter ///////////////////////////////////////

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public ProtoRecordContainer getRecordContainer() {
        return recordContainer;
    }

    public ConcurrentHashMap<String, Object> getFieldNameValueMap() {
        return fieldNameValueMap;
    }
}
