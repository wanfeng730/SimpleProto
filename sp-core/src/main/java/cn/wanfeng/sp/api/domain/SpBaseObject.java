package cn.wanfeng.sp.api.domain;

import cn.wanfeng.proto.constants.SpExceptionMessage;
import cn.wanfeng.proto.record.ProtoRecord;
import cn.wanfeng.proto.record.ProtoRecordContainer;
import cn.wanfeng.proto.record.ProtoRecordFactory;
import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.anno.Type;
import cn.wanfeng.sp.api.dataobject.SpDataObjectDO;
import cn.wanfeng.sp.api.model.SpPropertyValue;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SimpleExceptionCode;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.exception.SpObjectStoreException;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

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

    protected LinkedHashMap<String, SpPropertyValue> propertyValueContainer;

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

        assertIdNotNull(id);
        // 从数据库获取此id的字段
        SpDataObjectDO dataObjectDO = this.session.databaseStorage().findObjectById(SimpleProtoConfig.dataTable, id);
        assertIdFoundFromDatabase(id, dataObjectDO);

        // 读取数据库数据，构建对象
        readSpDataObjectDO(dataObjectDO);
    }

    protected SpBaseObject(SpSession session, @NotNull SpDataObjectDO objectDO){
        this.session = session;
        this.isNewObject = false;
        initInternalContainer();
        // 读取数据库数据，构建对象
        readSpDataObjectDO(objectDO);
    }

    protected void readSpDataObjectDO(@NotNull SpDataObjectDO objectDO){
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
                // 如果有set方法，优先调用set方法，这样可以在领域对象中执行到重写的set方法便于一些字段的同步更新（例如json和List的转换）
                Method setter = SimpleReflectUtils.getSetterMethodByField(this.getClass(), declaredField);
                if(Objects.isNull(setter)){
                    declaredField.set(this, value);
                }else{
                    setter.invoke(this, value);
                }
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
        return UlidUtil.generateUlid();
    }

    @Override
    public void store() {
        // 保存前的一些操作
        beforeStoreOperations();
        // 若为新数据则新建，否则更新
        if (isNewObject) {
            createObjectToStorage();
        } else {
            updateObjectToStorage();
        }
        // 保存后的一些操作
        afterStoreOperations();
    }

    /**
     * 保存对象前的操作，设置为public提供给批量操作
     */
    @Override
    public void beforeStoreOperations(){
        //根据是否为新对象执行store操作
        isNewObject = Objects.isNull(this.id);
        if (isNewObject) {
            //校验处理
            beforeCreateStoreAssertAndHandle();
            // 生成主键id
            // generateIncreaseId();
            generateFireworksId();
            // 生成id后校验处理
            afterGenerateIdAssertAndHandle();
        } else {
            //校验处理
            beforeUpdateStoreAssertAndHandle();
        }
        // 将基础对象的属性放到indexNoRecordMap和fieldNameValueMap
        putThisPropertyToContainers();
        putDeclaredPropertyToContainerAndValueMap();
    }

    /**
     * 保存对象后的操作，设置为public提供给批量操作
     */
    public void afterStoreOperations(){
        isNewObject = false;
    }


    /**
     * 新建保存对象前的校验和处理，用于给子类重写方法
     */
    protected void beforeCreateStoreAssertAndHandle(){
        assertTypeValueEqualsAnnotation();
    }

    /**
     * 生成id后，新建对象保存前校验和处理，用于子类重写方法
     */
    protected void afterGenerateIdAssertAndHandle(){

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

    private void assertTypeValueEqualsAnnotation(){
        Type typeAnnotation = this.getClass().getAnnotation(Type.class);
        if(Objects.isNull(typeAnnotation)){
            return;
        }
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
        propertyValueContainer.put(ID_FIELD, SpPropertyValue.build(Long.class, this.id));
        propertyValueContainer.put(TYPE_FIELD, SpPropertyValue.build(String.class, this.type));
        propertyValueContainer.put(NAME_FIELD, SpPropertyValue.build(String.class, this.name));
        propertyValueContainer.put(CREATE_DATE_FIELD, SpPropertyValue.build(Date.class, this.createDate));
        propertyValueContainer.put(MODIFY_DATE_FIELD, SpPropertyValue.build(Date.class, this.modifyDate));
        propertyValueContainer.put(IS_DELETE_FIELD, SpPropertyValue.build(Boolean.class, this.isDelete));
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
                    value = Objects.isNull(value) ? null : enumValueMethod.invoke(value);
                }
                //放入recordContainer
                ProtoRecord record = ProtoRecordFactory.buildProtoRecordByIndexAndValue(indexNo, fieldClass, value);
                recordContainer.putRecord(record);
                //放入propertyValueContainer
                SpPropertyValue spPropertyValue = SpPropertyValue.build(fieldClass, value);
                propertyValueContainer.put(fieldName, spPropertyValue);
            } catch (Exception e) {
                LogUtil.error("Get Property[index={}, name={}] Unknown Exception", indexNo, fieldName, e);
                throw new SpException(e);
            }
        }
    }

    protected void createObjectToStorage(){
        SpDataObjectDO dataObjectDO = generateDataObjectDO();
        try {
            session.createObjectToStorage(dataObjectDO, propertyValueContainer);
        } catch (Exception e) {
            LogUtil.error("对象创建失败，数据已回滚，失败原因", e);
            throw new SpException(SimpleExceptionCode.UNKNOWN_EXCEPTION);
        }
    }

    protected void updateObjectToStorage(){
        // 该对象更新到数据库
        SpDataObjectDO dataObjectDO = generateDataObjectDO();
        try {
            session.updateObjectToStorage(dataObjectDO, propertyValueContainer);
        } catch (Exception e) {
            LogUtil.error("对象更新失败，数据已回滚，失败原因", e);
            throw new SpException(SimpleExceptionCode.UNKNOWN_EXCEPTION);
        }
    }

    private void generateFireworksId(){
        Type typeAnnotation = this.getClass().getAnnotation(Type.class);
        if(Objects.isNull(typeAnnotation)){
            throw new SpException("对象生成烟花id失败，未指定@Type注解");
        }
        int type = typeAnnotation.number();
        this.id = FireworksIDGenerator.generate_1(type);
        LogUtil.debug("已生成烟花id: {} (type = {})", this.id, type);
    }


    @Override
    public void remove() {
        if (isNewObject) {
            LogUtil.info("对象[id={}, type={}, name={}]为新建对象，未保存到数据库，无需删除", this.id, this.type, this.name);
        } else {
            // 在数据库中删除
            removeObjectFromStorage();
        }
    }

    /**
     * 将对象从数据库删除，用于子类重写方法
     */
    protected void removeObjectFromStorage(){
        removeObjectById(id);
    }

    private void removeObjectById(Long id){
        try {
            session.removeObjectFromStorage(id);
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
    public Date getModifyDate() {
        return modifyDate;
    }

    @Override
    public Boolean getDelete() {
        return isDelete;
    }

    /**
     * 是否为新建对象
     */
    @Override
    public boolean isNewObject() {
        return Objects.isNull(id);
    }

    /**
     * 生成数据库存储实体对象
     */
    @Override
    public SpDataObjectDO generateDataObjectDO() {
        byte[] data = ProtoRecordFactory.writeRecordListToBytes(recordContainer);
        return SpObjectConvertUtils.convertSpBaseObjectToDO(this, data);
    }

    /**
     * 获取高级搜索存储数据
     */
    @Override
    public Map<String, SpPropertyValue> getDocument() {
        return this.propertyValueContainer;
    }

    // Some Assertions

    protected static void assertIdNotNull(Long id) {
        if (Objects.isNull(id)) {
            throw new SpObjectStoreException("Property id is NULL, Store Object Failed");
        }
    }

    protected static void assertIdFoundFromDatabase(Long id, SpDataObjectDO dataObjectDO) {
        if (Objects.isNull(dataObjectDO)) {
            throw new SpException(SimpleExceptionCode.OBJECT_ID_NOT_FOUND, id);
        }
    }
}
