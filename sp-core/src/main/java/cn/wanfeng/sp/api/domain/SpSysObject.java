package cn.wanfeng.sp.api.domain;


import cn.wanfeng.proto.record.ProtoRecord;
import cn.wanfeng.proto.record.ProtoRecordFactory;
import cn.wanfeng.sp.api.dataobject.SpDataObjectDO;
import cn.wanfeng.sp.api.enums.SystemTag;
import cn.wanfeng.sp.api.model.SpPropertyValue;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.exception.SpObjectStoreException;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.LogUtil;
import cn.wanfeng.sp.util.SpObjectConvertUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
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

    private static Logger logger = LogUtil.getSimpleProtoLogger();

    protected String systemTag;

    protected String path;

    protected Long parentId;

    protected String parentPath;

    /**
     * 父级系统对象
     */
    protected ISpSysObject parentSysObject;

    public SpSysObject(SpSession session, String type, String name) {
        super(session, type, name);
    }

    public SpSysObject(SpSession session, String type, String name, ISpSysObject parentSysObject, SystemTag systemTag){
        super(session, type, name);
        setPathAndParentInfo(parentSysObject);
        setSystemTag(systemTag);
    }

    public SpSysObject(SpSession session, String type, String name, Long parentId, SystemTag systemTag){
        super(session, type, name);
        SpSysObject parentSysObject = new SpSysObject(session, parentId);
        setPathAndParentInfo(parentSysObject);
        setSystemTag(systemTag);
    }

    public SpSysObject(SpSession session, Long id) {
        super(session, findObjectDOByIdAssertExist(session, id));
    }

    protected SpSysObject(SpSession session, SpDataObjectDO sysObjectDO){
        super(session, sysObjectDO);
    }



    @Override
    protected void beforeCreateStoreAssertAndHandle() {
        super.beforeCreateStoreAssertAndHandle();

        // tag必须是可支持的类型
        assertTagIsSupported();
        // name不能为空
        assertNameNotBlank();
        // 如果没有设定父对象信息，默认挂接到根对象下
        linkRootFolderIfNoParent();
        // 路径查重校验
        assertPathUnique();
    }

    @Override
    protected void beforeUpdateStoreAssertAndHandle() {
        super.beforeUpdateStoreAssertAndHandle();

        // tag必须是可支持的类型
        assertTagIsSupported();
        // name不能为空
        assertNameNotBlank();
        // 如果没有设定父对象信息，默认挂接到根对象下
        linkRootFolderIfNoParent();
    }

    /**
     * 将对象从数据库删除，用于子类重写方法
     */
    @Override
    protected void removeObjectFromStorage() {
        // 查询出下级所有的sysObject的id（级联）
        // 若为文件则一定没有下级，减少查询消耗
        List<SpDataObjectDO> childObjectDOList = SystemTag.FILE.getValue().equals(systemTag) ? new ArrayList<>() : findAllChildSysObject();
        if(CollectionUtils.isNotEmpty(childObjectDOList)){
            for (SpDataObjectDO spDataObjectDO : childObjectDOList) {
                if(SystemTag.FILE.getValue().equals(spDataObjectDO.getTag())){
                    //若为文件，执行SpFile类的删除方法
                    SpFile spFile = new SpFile(session, spDataObjectDO.getId());
                    spFile.remove();
                }else {
                    //若为文件夹，直接构造SpBaseObject删除（如果使用SpFolder的删除方法，会重复查询子对象，增加消耗）
                    SpBaseObject folder = new SpBaseObject(session, spDataObjectDO.getId());
                    folder.remove();
                    // SpFolder spFolder = new SpFolder(session, spDataObjectDO.getId());
                    // spFolder.remove();
                }
            }
        }
        super.removeObjectFromStorage();
    }

    @Override
    protected void readContainerDataToThis() {
        readContainerToBaseObjectProperty();
        readContainerToSysObjectProperty();
    }

    @Override
    protected void putThisPropertyToContainers(){
        putBasePropertyToContainers();
        putSysPropertyToContainers();
    }



    @Override
    public String getSystemTag() {
        return this.systemTag;
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
    public void setSystemTag(SystemTag systemTag) {
        this.systemTag = systemTag.getValue();
    }

    @Override
    public void setName(String name) {
        if(!isNewObject){
            throw new SpObjectStoreException("不支持二次修改SpSysObject及子类的名称");
        }
    }

    @Override
    public void move(Long parentId) {
        SpDataObjectDO parentObjectDO = session.databaseStorage().findObjectById(SimpleProtoConfig.dataTable, parentId);
        assertIdFoundFromDatabase(parentId, parentObjectDO);
        SpSysObject parentSysObject = new SpSysObject(session, parentObjectDO);
        move(parentSysObject);
    }

    @Override
    public void move(String parentPath) {
        SpDataObjectDO parentObjectDO = session.databaseStorage().findObjectByPath(SimpleProtoConfig.dataTable, parentPath);
        assertPathFoundFromDatabase(parentPath, parentObjectDO);
        SpSysObject parentSysObject = new SpSysObject(session, parentObjectDO);
        move(parentSysObject);
    }

    @Override
    public void move(ISpSysObject parentSysObject) {
        // @luozh-code: 移动需要递归修改下级所有对象的路径
        setPathAndParentInfo(parentSysObject);
    }

    private void setPathAndParentInfo(ISpSysObject parentSysObject) {
        if (Objects.nonNull(parentSysObject)) {
            //父对象必须是已保存到数据库
            assertParentObjectStored(parentSysObject);
            this.parentSysObject = parentSysObject;
            this.parentId = parentSysObject.getId();
            this.parentPath = parentSysObject.getPath();
            this.path = this.parentPath + pathSeparator + this.name;
        }
    }

    private void putSysPropertyToContainers(){
        //放入recordContainer，生成数据库存储的data数据
        ProtoRecord tagRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(TAG_INDEX, this.systemTag);
        recordContainer.putRecord(tagRecord);
        ProtoRecord pathRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(PATH_INDEX, this.path);
        recordContainer.putRecord(pathRecord);
        ProtoRecord parentIdRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(PARENT_ID_INDEX, this.parentId);
        recordContainer.putRecord(parentIdRecord);
        ProtoRecord parentPathRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(PARENT_PATH_INDEX, this.parentPath);
        recordContainer.putRecord(parentPathRecord);

        //放入propertyValueContainer，生成高级搜索保存的数据
        propertyValueContainer.put(TAG_FIELD, SpPropertyValue.build(String.class, this.systemTag));
        propertyValueContainer.put(PATH_FIELD, SpPropertyValue.build(String.class, this.path));
        propertyValueContainer.put(PARENT_ID_FIELD, SpPropertyValue.build(Long.class, this.parentId));
        propertyValueContainer.put(PARENT_PATH_FIELD, SpPropertyValue.build(String.class, this.parentPath));
    }


    private void readContainerToSysObjectProperty(){
        ConcurrentHashMap<Integer, ProtoRecord> indexNoRecordMap = recordContainer.getIndexNoRecordMap();
        systemTag = (String) indexNoRecordMap.get(TAG_INDEX).getValue();
        path = (String) indexNoRecordMap.get(PATH_INDEX).getValue();
        parentId = (Long) indexNoRecordMap.get(PARENT_ID_INDEX).getValue();
        parentPath = (String) indexNoRecordMap.get(PARENT_PATH_INDEX).getValue();
    }

    private void assertNameNotBlank(){
        if(StringUtils.isBlank(this.name)){
            throw new SpException("Property[name] must not be null, Please use Method setName() to update [name]");
        }
    }

    private void assertTagIsSupported(){
        if(StringUtils.isBlank(this.systemTag)){
            throw new SpException("Property[tag] must be not null, Please use Method setTag() to update [tag]");
        }
        SystemTag systemTag = SystemTag.toEnum(this.systemTag);
        if(SystemTag.NONE.equals(systemTag)){
            throw new SpException("Property[tag] is not Supported, Please use Method setTag() to change [tag]");
        }
    }

    private void assertPathUnique(){
        SpDataObjectDO existPathObject = session.databaseStorage().findObjectByPath(SimpleProtoConfig.dataTable, this.path);
        if(Objects.nonNull(existPathObject)){
            throw new SpException("路径已存在[%s]，保存到数据表[%s]失败", path, SimpleProtoConfig.dataTable);
        }
    }

    protected static void assertParentObjectStored(ISpSysObject parentSysObject){
        if(parentSysObject.isNewObject()){
            throw new SpException("父对象未保存，保存当前对象前先对父对象调用store()方法保存");
        }
    }

    protected static SpDataObjectDO findObjectDOByIdAssertExist(SpSession session, Long id){
        SpDataObjectDO objectDO = session.databaseStorage().findObjectById(SimpleProtoConfig.dataTable, id);
        if(Objects.isNull(objectDO)){
            throw new SpException("id(%s)不存在，请确认对象是否已被删除", id);
        }
        return objectDO;
    }

    private void linkRootFolderIfNoParent(){
        if(StringUtils.isBlank(this.parentPath)){
            this.parentId = SimpleProtoConfig.rootSysObjectId;
            this.parentPath = SimpleProtoConfig.rootSysObjectPath;
            this.path = this.parentPath + this.name;
            logger.warn("系统对象[name = {}]未指定父对象，将挂接在根路径下，最终路径为[{}]", name, path);
        }
    }



    @Override
    protected void createObjectToStorage() {
        // 序列化，构造对象数据保存到数据库
        byte[] data = ProtoRecordFactory.writeRecordListToBytes(recordContainer);
        SpDataObjectDO sysObjectDO = SpObjectConvertUtils.convertSpSysObjectToDO(this, data);
        try {
            session.createObjectToStorage(sysObjectDO, propertyValueContainer);
        } catch (Exception e) {
            LogUtil.error("对象创建失败，数据已回滚，失败原因", e);
        }
    }

    @Override
    protected void updateObjectToStorage() {
        // 所有字段序列化成字节数组
        byte[] data = ProtoRecordFactory.writeRecordListToBytes(recordContainer);
        SpDataObjectDO sysObjectDO = SpObjectConvertUtils.convertSpSysObjectToDO(this, data);
        try {
            session.updateObjectToStorage(sysObjectDO, propertyValueContainer);
        } catch (Exception e) {
            LogUtil.error("对象更新失败，数据已回滚，失败原因", e);
        }
    }

    private List<SpDataObjectDO> findAllChildSysObject(){
        return session.databaseStorage().findObjectByLikePath(SimpleProtoConfig.dataTable, this.path + "/%", 20000);
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

    private void assertPathFoundFromDatabase(String path, SpDataObjectDO objectDO){
        if(Objects.isNull(objectDO)){
            throw new SpObjectNotFoundException("Not Found Path[%s] from Database", path);
        }
    }
}
