package cn.wanfeng.sp.api.domain;

import cn.wanfeng.proto.constants.SpExceptionMessage;
import cn.wanfeng.proto.record.ProtoRecord;
import cn.wanfeng.proto.record.ProtoRecordContainer;
import cn.wanfeng.proto.record.ProtoRecordFactory;
import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.anno.Type;
import cn.wanfeng.sp.api.dataobject.SpBaseObjectDO;
import cn.wanfeng.sp.api.dataobject.SpSettingsDO;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.exception.SpObjectNotFoundException;
import cn.wanfeng.sp.exception.SpObjectStoreException;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.LogUtil;
import cn.wanfeng.sp.util.SimpleReflectUtils;
import cn.wanfeng.sp.util.SpObjectConvertUtils;
import com.github.f4b6a3.ulid.UlidCreator;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date: 2024-04-02 23:34
 * @author: luozh
 * @description: simpleproto基础对象
 * @since: 1.0
 */
public class SpBaseObject implements ISpBaseObject{

    protected SpSession session;

    protected Long id;

    protected String type;

    protected String name;

    protected Date createDate;

    protected Date modifyDate;

    protected Boolean isDelete;

    protected ProtoRecordContainer recordContainer;

    protected HashMap<String, Integer> propertyIndexNoContainer;

    protected LinkedHashMap<String, Object> propertyValueContainer;

    /**
     * 该对象是否为新建的对象（该id未在数据库中存在）
     */
    protected boolean isNewObject;

    public SpBaseObject(SpSession session, String type) {
        this(session, type, defaultUlidName());
    }

    public SpBaseObject(SpSession session, String type, String name) {
        this.session = session;
        this.isNewObject = true;
        initBaseObject(type, name);
        initInternalContainer();
        //校验属性的indexNo和fieldName不重复，并生成映射关系
        assertProtoFieldUniqueAndBuildIndexNoMap();
    }

    public SpBaseObject(SpSession session, Long id) {
        this.session = session;
        this.isNewObject = false;
        initInternalContainer();

        assertStoreObjectIdNotNull(id);
        // 从数据库获取此id的字段
        SpBaseObjectDO objectDO = this.session.databaseStorage().findObjectById(SimpleProtoConfig.dataTable, id);
        assertIdFoundFromDatabase(id, objectDO);

        //校验属性的indexNo和fieldName不重复，并生成映射关系
        assertProtoFieldUniqueAndBuildIndexNoMap();

        // 反序列化proto二进制数据
        this.recordContainer = ProtoRecordFactory.readBytesToRecordList(objectDO.getData());
        LogUtil.debug(recordContainer.getIndexNoRecordMap().toString());

        // 读取container中的数据并设置到本对象的属性中
        readContainerDataToThis();
        // 读取container中的数据设置到注解属性中
        readContainerToAnnotationProperty();
    }

    protected SpBaseObject(SpSession session, @NotNull SpBaseObjectDO objectDO){
        this.session = session;
        this.isNewObject = false;
        initInternalContainer();

        //校验属性的indexNo和fieldName不重复，并生成映射关系
        assertProtoFieldUniqueAndBuildIndexNoMap();

        // 反序列化proto二进制数据
        this.recordContainer = ProtoRecordFactory.readBytesToRecordList(objectDO.getData());
        LogUtil.debug(recordContainer.getIndexNoRecordMap().toString());

        // 读取container中的数据并设置到本对象的属性中
        readContainerDataToThis();
        // 读取container中的数据设置到注解属性中
        readContainerToAnnotationProperty();
    }

    private void assertProtoFieldUniqueAndBuildIndexNoMap(){
        Set<String> addFieldNameSet = new HashSet<>();
        Set<Integer> addIndexNoSet = new HashSet<>();
        Field[] fields = SimpleReflectUtils.getProtoFieldAnnotationFields(this.getClass());
        for (Field field : fields) {
            ProtoField protoField = field.getAnnotation(ProtoField.class);
            int indexNo = protoField.index();
            String fieldName = protoField.name();

            if(addFieldNameSet.contains(fieldName) || addIndexNoSet.contains(indexNo)){
                throw new SpException(SpExceptionMessage.protoFieldNameDuplicate(indexNo, fieldName));
            }
            addFieldNameSet.add(fieldName);
            addIndexNoSet.add(indexNo);

            this.propertyIndexNoContainer.put(fieldName, indexNo);
        }
    }

    /**
     * 将protoRecord设置到对应的SpBaseObject的成员属性上
     */
    protected void readContainerToBaseObjectProperty() {
        ConcurrentHashMap<Integer, ProtoRecord> indexNoRecordMap = recordContainer.getIndexNoRecordMap();
        id = (Long) indexNoRecordMap.get(ID_INDEX).getValue();
        type = (String) indexNoRecordMap.get(TYPE_INDEX).getValue();
        name = (String) indexNoRecordMap.get(NAME_INDEX).getValue();
        createDate = (Date) indexNoRecordMap.get(CREATE_DATE_INDEX).getValue();
        modifyDate = (Date) indexNoRecordMap.get(MODIFY_DATE_INDEX).getValue();
        isDelete = (Boolean) indexNoRecordMap.get(IS_DELETE_INDEX).getValue();
    }

    protected void readContainerToAnnotationProperty() {
        ConcurrentHashMap<Integer, ProtoRecord> indexNoRecordMap = recordContainer.getIndexNoRecordMap();
        Field[] protoField = SimpleReflectUtils.getProtoFieldAnnotationFields(this.getClass());
        for (Field declaredField : protoField) {
            int indexNo = declaredField.getAnnotation(ProtoField.class).index();
            String fieldName = declaredField.getAnnotation(ProtoField.class).name();
            Class<?> fieldClass = declaredField.getType();
            if (!indexNoRecordMap.containsKey(indexNo)) {
                continue;
            }
            Object value;
            declaredField.setAccessible(true);
            try {
                value = indexNoRecordMap.get(indexNo).getValue();
                if(fieldClass.isEnum()){
                    //获取ProtoEnumConstructor方法,转换为枚举类实例
                    Method constructorMethod = SimpleReflectUtils.getProtoEnumConstructorMethod(fieldClass);
                    value = Objects.isNull(constructorMethod) ? null : constructorMethod.invoke(null, value);
                }
                declaredField.set(this, value);
                LogUtil.debug("Set DeclaredField Property: {}={}", declaredField.getName(), declaredField.get(this));
            } catch (Exception e) {
                LogUtil.error("Set Property[index={}, name={}] Unknown Exception", indexNo, fieldName, e);
                throw new SpException(e);
            }
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
    private void initInternalContainer() {
        this.recordContainer = ProtoRecordContainer.emptyContainer();
        this.propertyIndexNoContainer = new HashMap<>(8);
        this.propertyValueContainer = new LinkedHashMap<>(8);
    }

    /**
     * 生成默认的对象名称
     *
     * @return ULID
     */
    private static String defaultUlidName() {
        return UlidCreator.getUlid().toString();
    }

    @Override
    public void store() {
        //根据是否为新对象执行store操作
        isNewObject = Objects.isNull(this.id);
        if (isNewObject) {
            //校验处理
            beforeCreateStoreAssertAndHandle();

            // 生成主键id
            generateIncreaseId();
            // 将基础对象的属性放到indexNoRecordMap和fieldNameValueMap
            putThisPropertyToContainers();
            putDeclaredPropertyToContainerAndValueMap();
            // 新建数据到数据库（事务：数据库存储新建、设置自增主键id、高级搜索存储新建）
            createObjectToStorage();
            isNewObject = false;
        } else {
            //校验处理
            beforeUpdateStoreAssertAndHandle();

            // 将继承类中的属性放到indexNoRecordMap和fieldNameValueMap
            putThisPropertyToContainers();
            putDeclaredPropertyToContainerAndValueMap();
            // 更新数据(事务：数据库存储更新、高级搜索存储更新）
            updateObjectToStorage();
        }

    }

    /**
     * 新建保存对象前的校验和处理，用于给子类重写方法
     */
    protected void beforeCreateStoreAssertAndHandle(){
        assertTypeValueEqualsAnnotation();
    }

    /**
     * 更新保存对象前的校验和处理，用于给子类重写方法
     */
    protected void beforeUpdateStoreAssertAndHandle(){
        updateModifyDate();
    }

    /**
     * 将本对象属性放到container中，用于给子类重写方法
     */
    protected void putThisPropertyToContainers(){
        putBasePropertyToContainers();
    }

    /**
     * 读取数据设置到对象中，用于子类重写方法
     */
    protected void readContainerDataToThis() {
        // 读取并设置该对象的基本属性
        readContainerToBaseObjectProperty();
    }

    /**
     * 删除对象前的校验和处理，用于给子类重写方法
     */
    protected void beforeRemoveAssertAndHandle(){

    }

    private void assertTypeValueEqualsAnnotation(){
        Type typeAnnotation = this.getClass().getAnnotation(Type.class);
        String typeAnnoValue = typeAnnotation.value();
        if(!StringUtils.equals(this.type, typeAnnoValue)){
            throw new SpException(String.format("保存对象异常，type的值必须和注解@Type标注的值[%s]一致，当前type=[%s]", typeAnnoValue, this.type));
        }
    }

    private void updateModifyDate() {
        this.modifyDate = new Date();
    }

    protected void putBasePropertyToContainers(){
        //放入recordContainer，生成数据库存储的data数据
        ProtoRecord idRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(ID_INDEX, this.id);
        recordContainer.putRecord(idRecord);
        ProtoRecord typeRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(TYPE_INDEX, this.type);
        recordContainer.putRecord(typeRecord);
        ProtoRecord nameRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(NAME_INDEX, this.name);
        recordContainer.putRecord(nameRecord);
        ProtoRecord createDateRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(CREATE_DATE_INDEX, this.createDate);
        recordContainer.putRecord(createDateRecord);
        ProtoRecord modifyDateRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(MODIFY_DATE_INDEX, this.modifyDate);
        recordContainer.putRecord(modifyDateRecord);
        ProtoRecord isDeleteRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(IS_DELETE_INDEX, this.isDelete);
        recordContainer.putRecord(isDeleteRecord);

        //放入propertyValueContainer，生成高级搜索保存的数据
        propertyValueContainer.put(ID_FIELD, this.id);
        propertyValueContainer.put(TYPE_FIELD, this.type);
        propertyValueContainer.put(NAME_FIELD, this.name);
        propertyValueContainer.put(CREATE_DATE_FIELD, this.createDate);
        propertyValueContainer.put(MODIFY_DATE_FIELD, this.modifyDate);
        propertyValueContainer.put(IS_DELETE_FIELD, this.isDelete);
    }

    protected void putDeclaredPropertyToContainerAndValueMap(){
        Field[] fields = SimpleReflectUtils.getProtoFieldAnnotationFields(this.getClass());

        for (Field field : fields) {
            field.setAccessible(true);
            ProtoField protoField = field.getAnnotation(ProtoField.class);
            int indexNo = protoField.index();
            String fieldName = protoField.name();
            try {
                Object value = field.get(this);
                Class<?> fieldClass = field.getType();
                if(fieldClass.isEnum()){
                    Method enumValueMethod = SimpleReflectUtils.getProtoEnumValueMethod(fieldClass);
                    assert enumValueMethod != null;
                    fieldClass = enumValueMethod.getReturnType();
                    value = enumValueMethod.invoke(value);
                }
                //放入recordContainer
                ProtoRecord record = ProtoRecordFactory.buildProtoRecordByIndexAndValue(indexNo, fieldClass, value);
                recordContainer.putRecord(record);
                //放入propertyValueContainer
                if(Objects.nonNull(value)){
                    propertyValueContainer.put(fieldName, value);
                }
            } catch (Exception e) {
                LogUtil.error("Get Property[index={}, name={}] Unknown Exception", indexNo, fieldName, e);
                throw new SpException(e);
            }
        }
    }

    protected void createObjectToStorage(){
        // 序列化，构造对象数据保存到数据库
        byte[] data = ProtoRecordFactory.writeRecordListToBytes(recordContainer);
        SpBaseObjectDO baseObjectDO = SpObjectConvertUtils.convertSpBaseObjectToDO(this, data);
        try {
            session.createBaseObjectToStorage(baseObjectDO, propertyValueContainer);
        } catch (Exception e) {
            LogUtil.error("对象创建失败，数据已回滚，失败原因", e);
        }
    }

    protected void updateObjectToStorage(){
        // 所有字段序列化成字节数组
        byte[] data = ProtoRecordFactory.writeRecordListToBytes(recordContainer);
        // 该对象更新到数据库
        SpBaseObjectDO baseObjectDO = SpObjectConvertUtils.convertSpBaseObjectToDO(this, data);
        try {
            session.updateBaseObjectToStorage(baseObjectDO, propertyValueContainer);
        } catch (Exception e) {
            LogUtil.error("对象更新失败，数据已回滚，失败原因", e);
        }
    }



    private void generateIncreaseId() {
        //从redis获取当前自增的id值，若没有则从数据库加载
        boolean locked = session.cacheOperator().lockRetryable(OBJECT_ID_INCREASE_NAME);
        if(locked){
            try {
                SpSettingsDO idIncreaseDO = session.databaseStorage().findSettingsByName(SimpleProtoConfig.settingsTable, OBJECT_ID_INCREASE_NAME);
                this.id = Objects.isNull(idIncreaseDO) ? 1L : idIncreaseDO.getIncreaseLong() + 1;
                // 自增后的id保存到数据库
                SpSettingsDO settingsDO = new SpSettingsDO();
                settingsDO.setName(OBJECT_ID_INCREASE_NAME);
                settingsDO.setIncreaseLong(this.id);
                //更新设置表
                if(Objects.isNull(idIncreaseDO)){
                    session.databaseStorage().insertSettings(SimpleProtoConfig.settingsTable, settingsDO);
                }else {
                    session.databaseStorage().updateSettings(SimpleProtoConfig.settingsTable, settingsDO);
                }
            } catch (Exception e) {
                throw new SpException("生成自增id时出现未知异常", e);
            } finally {
                session.cacheOperator().unLock(OBJECT_ID_INCREASE_NAME);
            }
        }
    }


    @Override
    public void remove() {
        if (isNewObject) {
            LogUtil.info("对象[id={}, type={}, name={}]为新建对象，未保存到数据库，无需删除", this.id, this.type, this.name);
        } else {
            // 校验处理
            beforeRemoveAssertAndHandle();
            // 在数据库中删除
            removeObjectFromStorage();
        }
    }

    private void removeObjectFromStorage(){
        try {
            session.removeObjectFromStorage(this.id);
        } catch (Exception e) {
            LogUtil.error("对象删除失败，数据已回滚，失败原因", e);
        }
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public Boolean getDelete() {
        return isDelete;
    }

    @Override
    public Date getModifyDate() {
        return modifyDate;
    }


    // Some Assertions

    protected void assertStoreObjectIdNotNull(Long id) {
        if (Objects.isNull(id)) {
            throw new SpObjectStoreException("Property id is NULL, Store Object Failed");
        }
    }

    protected void assertIdFoundFromDatabase(Long id, SpBaseObjectDO spBaseObjectDO) {
        if (Objects.isNull(spBaseObjectDO)) {
            throw new SpObjectNotFoundException("Not Found id[%d] from Database", id);
        }
    }
}
