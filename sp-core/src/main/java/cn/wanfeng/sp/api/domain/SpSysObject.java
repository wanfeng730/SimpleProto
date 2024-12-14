package cn.wanfeng.sp.api.domain;


import cn.wanfeng.proto.record.ProtoRecord;
import cn.wanfeng.proto.record.ProtoRecordFactory;
import cn.wanfeng.sp.api.enums.SystemTag;
import cn.wanfeng.sp.api.dataobject.SpSysObjectDO;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.exception.SpObjectNotFoundException;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.LogUtil;
import cn.wanfeng.sp.util.SpObjectConvertUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
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

    /**
     * 父级系统对象
     */
    private ISpSysObject parentSysObject;

    public SpSysObject(SpSession session, String type, String name) {
        super(session, type, name);
    }

    public SpSysObject(SpSession session, String type, String name, ISpSysObject parentSysObject, SystemTag systemTag){
        super(session, type, name);
        updateByParentSysObject(parentSysObject);
        updateBySystemTag(systemTag);
    }

    public SpSysObject(SpSession session, Long id) {
        super(session, session.databaseStorage().findSysObjectById(SimpleProtoConfig.dataTable, id));
    }

    protected SpSysObject(SpSession session, SpSysObjectDO sysObjectDO){
        super(session, sysObjectDO);
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
        SpSysObjectDO parentObjectDO = session.databaseStorage().findSysObjectById(SimpleProtoConfig.dataTable, parentId);
        assertIdFoundFromDatabase(parentId, parentObjectDO);
        SpSysObject parentSysObject = new SpSysObject(session, parentObjectDO);
        move(parentSysObject);
    }

    @Override
    public void move(String parentPath) {
        SpSysObjectDO parentObjectDO = session.databaseStorage().findSysObjectByPath(SimpleProtoConfig.dataTable, parentPath);
        assertPathFoundFromDatabase(parentPath, parentObjectDO);
        SpSysObject parentSysObject = new SpSysObject(session, parentObjectDO);
        move(parentSysObject);
    }

    @Override
    public void move(ISpSysObject parentSysObject) {
        updateByParentSysObject(parentSysObject);
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

        if(isNewObject){
            // 路径查重校验
            assertPathUnique();
            //生成主键id
            generateIncreaseId();
            // 将基础对象的属性放到container中
            putBasePropertyToContainers();
            // 将系统对象的属性放到container中
            putSysPropertyToContainers();

            putDeclaredPropertyToContainerAndValueMap();
            // 新建数据到数据库
            createObjectToStorage();
            isNewObject = false;
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
        //级联查询出下级所有的sysObject的id
        List<SpSysObjectDO> childObjectDOList = findAllChildSysObject();
        if(CollectionUtils.isNotEmpty(childObjectDOList)){
            for (SpSysObjectDO spSysObjectDO : childObjectDOList) {
                removeObjectById(spSysObjectDO.getId());
            }
        }
        //删除自身
        removeObjectById(this.id);
    }

    @Override
    protected void readContainerDataToThis() {
        readRecordMapToBaseObjectProperty();
        readRecordMapToSysObjectProperty();
        readRecordMapToAnnotationProperty();
    }

    protected void updateByParentSysObject(ISpSysObject parentSysObject){
        if (Objects.nonNull(parentSysObject)){
            this.parentSysObject = parentSysObject;
            this.parentId = parentSysObject.getId();
            this.parentPath = parentSysObject.getPath();
            this.path = this.parentPath + pathSeparator + this.name;
        }
    }

    protected void updateBySystemTag(SystemTag systemTag){
        if (Objects.nonNull(systemTag)){
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
        SpSysObjectDO existPathObject = session.databaseStorage().findSysObjectByPath(SimpleProtoConfig.dataTable, this.path);
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
            session.createSysObjectToStorage(sysObjectDO, propertyValueContainer);
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
            session.updateSysObjectToStorage(sysObjectDO, propertyValueContainer);
        } catch (Exception e) {
            LogUtil.error("对象更新失败，数据已回滚，失败原因", e);
        }
    }

    public List<SpSysObjectDO> findAllChildSysObject(){
        return session.databaseStorage().findSysObjectByLikePath(SimpleProtoConfig.dataTable, this.path + "/%");
    }

    protected void removeObjectById(Long id){
        try {
            session.removeObjectFromStorage(id);
            LogUtil.debug("已删除id[{}]", id);
        } catch (Exception e) {
            LogUtil.error("对象删除失败，数据已回滚，失败原因", e);
        }
    }


    // some assertions

    protected void assertPathFoundFromDatabase(String path, SpSysObjectDO objectDO){
        if(Objects.isNull(objectDO)){
            throw new SpObjectNotFoundException("Not Found Path[%s] from Database", path);
        }
    }
}
