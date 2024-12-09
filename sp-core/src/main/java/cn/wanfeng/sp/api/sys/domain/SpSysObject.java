package cn.wanfeng.sp.api.sys.domain;


import cn.wanfeng.proto.record.ProtoRecord;
import cn.wanfeng.proto.record.ProtoRecordFactory;
import cn.wanfeng.sp.api.base.domain.SpBaseObject;
import cn.wanfeng.sp.api.base.object.SpSysObjectDO;
import cn.wanfeng.sp.api.sys.enums.SystemTag;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.LogUtil;
import cn.wanfeng.sp.util.SpObjectConvertUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date: 2024-12-08 22:37
 * @author: luozh.wanfeng
 * @description: SimpleProto 系统对象
 * @since: 1.0
 */
public class SpSysObject extends SpBaseObject implements ISpSysObject{

    private String tag;

    private String path;

    private Long parentId;

    private String parentPath;

    public SpSysObject(SpSession session, String type, String name) {
        super(session, type, name);
    }

    public SpSysObject(SpSession session, String type, String name, ISpSysObject parentSysObject, SystemTag systemTag){
        super(session, type, name);
        initSysObjectProperty(parentSysObject, systemTag);
    }

    public SpSysObject(SpSession session, Long id) {
        super(session, id);
    }


    @Override
    public void setTag(@NotNull SystemTag systemTag) {
        this.tag = systemTag.getValue();
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    @Override
    public String getParentPath() {
        return parentPath;
    }

    @Override
    public void move(Long parentId) {

    }

    @Override
    public void move(String parentPath) {

    }

    @Override
    public void move(ISpSysObject parentSysObject) {

    }

    @Override
    public void store() {
        assertTypeValueEqualsAnnotation();
        // tag必须是可支持的类型
        assertTagIsSupported();
        // name不能为空
        assertNameNotBlank();
        // 如果没有设定父对象信息，默认挂接到根对象下
        linkRootFolderIfNoParent();
        // LzhTODO: 名称查重校验
        assertPathUnique();

        if(isNewObject){
            //生成主键id
            generateIncreaseId();
            // 将基础对象的属性放到container中
            putBasePropertyToContainers();
            // 将系统对象的属性放到container中
            putSysPropertyToContainers();

            putDeclaredPropertyToContainerAndValueMap();
            // 新建数据到数据库
            createObjectToStorage();
        }else {
            // 修改时间刷新
            updateModifyDate();
            // 将基础对象的属性放到container中
            putBasePropertyToContainers();
            // 将系统对象的属性放到container中
            putSysPropertyToContainers();

            putDeclaredPropertyToContainerAndValueMap();
            // 更新数据(事务：数据库存储更新、高级搜索存储更新）
            updateObjectToStorage();
        }
    }

    @Override
    public void remove() {

    }

    @Override
    protected void readRecordMap() {
        readRecordMapToBaseObjectProperty();
        readRecordMapToSysObjectProperty();
        readRecordMapToAnnotationProperty();
    }

    protected void initSysObjectProperty(ISpSysObject parentSysObject, SystemTag systemTag){
        if(Objects.nonNull(parentSysObject)){
            this.parentId = parentSysObject.getId();
            this.parentPath = parentSysObject.getPath();
            this.path = this.parentPath + pathSeparator + this.name;
        }
        if(Objects.nonNull(systemTag)){
            this.tag = systemTag.getValue();
        }
    }

    protected void putSysPropertyToContainers(){
        //放入recordContainer，生成数据库存储的data数据
        ProtoRecord tagRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(TAG_INDEX, this.tag);
        recordContainer.putRecord(tagRecord);
        ProtoRecord pathRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(PATH_INDEX, this.path);
        recordContainer.putRecord(pathRecord);
        ProtoRecord parentIdRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(PARENT_ID_INDEX, this.parentId);
        recordContainer.putRecord(parentIdRecord);
        ProtoRecord parentPathRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(PARENT_PATH_INDEX, this.parentPath);
        recordContainer.putRecord(parentPathRecord);

        //放入propertyValueContainer，生成高级搜索保存的数据
        propertyValueContainer.put(TAG_FIELD, this.tag);
        propertyValueContainer.put(PATH_FIELD, this.path);
        propertyValueContainer.put(PARENT_ID_FIELD, this.parentId);
        propertyValueContainer.put(PARENT_PATH_FIELD, this.parentPath);
    }


    protected void readRecordMapToSysObjectProperty(){
        ConcurrentHashMap<Integer, ProtoRecord> indexNoRecordMap = recordContainer.getIndexNoRecordMap();
        tag = (String) indexNoRecordMap.get(TAG_INDEX).getValue();
        path = (String) indexNoRecordMap.get(PATH_INDEX).getValue();
        parentId = (Long) indexNoRecordMap.get(PARENT_ID_INDEX).getValue();
        parentPath = (String) indexNoRecordMap.get(PARENT_PATH_INDEX).getValue();
    }

    protected void assertNameNotBlank(){
        if(StringUtils.isBlank(this.name)){
            throw new SpException("Property[name] must not be null, Please use Method setName() to update [name]");
        }
    }

    protected void assertTagIsSupported(){
        if(StringUtils.isBlank(this.tag)){
            throw new SpException("Property[tag] must be not null, Please use Method setTag() to update [tag]");
        }
        SystemTag systemTag = SystemTag.toEnum(this.tag);
        if(SystemTag.NONE.equals(systemTag)){
            throw new SpException("Property[tag] is not Supported, Please use Method setTag() to change [tag]");
        }
    }

    protected void linkRootFolderIfNoParent(){
        if(StringUtils.isBlank(this.parentPath)){
            this.parentId = SimpleProtoConfig.rootSysObjectId;
            this.parentPath = SimpleProtoConfig.rootSysObjectPath;
            this.path = this.parentPath + this.name;
        }
    }

    protected void assertPathUnique(){
        SpSysObjectDO existPathObject = spSession.databaseStorage().findSysObjectByPath(SimpleProtoConfig.dataTable, this.path);
        if(Objects.nonNull(existPathObject)){
            throw new SpException("Path[%s] has already exist in Table[%s]", path, SimpleProtoConfig.dataTable);
        }
    }

    @Override
    protected void createObjectToStorage() {
        // 序列化，构造对象数据保存到数据库
        byte[] data = ProtoRecordFactory.writeRecordListToBytes(recordContainer);
        SpSysObjectDO sysObjectDO = SpObjectConvertUtils.convertSpSysObjectToDO(this, data);
        try {
            spSession.createSysObjectToStorage(sysObjectDO, propertyValueContainer);
        } catch (Exception e) {
            LogUtil.error("对象创建失败，数据已回滚，失败原因", e);
        }
    }

    @Override
    protected void updateObjectToStorage() {
        // 所有字段序列化成字节数组
        byte[] data = ProtoRecordFactory.writeRecordListToBytes(recordContainer);
        SpSysObjectDO sysObjectDO = SpObjectConvertUtils.convertSpSysObjectToDO(this, data);
        try {
            spSession.updateSysObjectToStorage(sysObjectDO, propertyValueContainer);
        } catch (Exception e) {
            LogUtil.error("对象更新失败，数据已回滚，失败原因", e);
        }
    }
}
