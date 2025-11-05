package cn.wanfeng.sp.api.domain;


import cn.wanfeng.proto.record.ProtoRecord;
import cn.wanfeng.proto.record.ProtoRecordFactory;
import cn.wanfeng.sp.api.enums.FileTag;
import cn.wanfeng.sp.api.enums.SystemTag;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.exception.SpFileStorageException;
import cn.wanfeng.sp.exception.SpObjectStoreException;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.FileUtils;
import cn.wanfeng.sp.util.InputStreamUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date: 2024-12-22 16:33
 * @author: luozh.wanfeng
 * @description: 文件对象
 * @since: 1.0
 */
public class SpFile extends SpSysObject implements ISpFile {

    protected String fileTag;
    protected Long fileSize;
    protected String suffix;
    protected String fileStorageKey;

    protected InputStream inputStream;
    protected boolean isNeedUploadFile = false;


    public SpFile(SpSession session, String type, String name, ISpSysObject parentSysObject, FileTag fileTag, InputStream inputStream) {
        super(session, type, name, parentSysObject, SystemTag.FILE);
        setSuffixByName(name);
        setFileTag(fileTag);
        setStorage(inputStream);
    }

    public SpFile(SpSession session, String type, String name, Long parentId, FileTag fileTag, InputStream inputStream) {
        super(session, type, name, parentId, SystemTag.FILE);
        setSuffixByName(name);
        setFileTag(fileTag);
        setStorage(inputStream);
    }

    public SpFile(SpSession session, String type, String name, InputStream inputStream) {
        super(session, type, name);
        setSystemTag(SystemTag.FILE);

        setSuffixByName(name);
        setStorage(inputStream);
    }

    public SpFile(SpSession session, String type, String name, String fileStorageKey) {
        super(session, type, name);
        setSystemTag(SystemTag.FILE);

        setSuffixByName(name);
        setStorage(fileStorageKey);
    }

    public SpFile(SpSession session, String type, String name) {
        super(session, type, name);
        setSystemTag(SystemTag.FILE);

        setSuffixByName(name);
    }

    /**
     * 根据id构造文件对象，默认不获取文件流
     * @param session session
     * @param id id
     */
    public SpFile(SpSession session, Long id) {
        super(session, id);
    }

    /**
     * 根据id构造文件对象
     * @param session session
     * @param id id
     * @param isGetStream 是否获取流
     */
    public SpFile(SpSession session, Long id, boolean isGetStream){
        super(session, id);
        if(isGetStream && StringUtils.isNotBlank(fileStorageKey)){
            setStorage(fileStorageKey);
        }
    }


    @Override
    protected void beforeCreateStoreAssertAndHandle() {
        super.beforeCreateStoreAssertAndHandle();

        assertNameIsFile();
        assertFileTagIsSupported();
        assertParentObjectIsFolder();
    }

    /**
     * 生成id后，新建对象保存前校验和处理，用于子类重写方法
     */
    @Override
    protected void afterGenerateIdAssertAndHandle() {
        super.afterGenerateIdAssertAndHandle();
        uploadInputStreamIfChanged();
    }

    @Override
    protected void beforeUpdateStoreAssertAndHandle() {
        super.beforeUpdateStoreAssertAndHandle();

        assertNameIsFile();
        assertFileTagIsSupported();
        assertParentObjectIsFolder();

        uploadInputStreamIfChanged();
    }

    /**
     * 将对象从数据库删除，用于子类重写方法
     */
    @Override
    protected void removeObjectFromStorage() {
        removeFileStorage();
        super.removeObjectFromStorage();
    }

    @Override
    protected void readContainerDataToThis() {
        super.readContainerDataToThis();
        readContainerToFileProperty();
    }

    @Override
    protected void putThisPropertyToContainers() {
        super.putThisPropertyToContainers();
        putFilePropertyToContainer();
    }

    @Override
    protected void removeObjectById(Long id) {
        removeFileStorage();
        super.removeObjectById(id);
    }

    private void setSuffixByName(String name){
        this.suffix = FileUtils.getSuffix(name);
    }

    private void readContainerToFileProperty() {
        ConcurrentHashMap<Integer, ProtoRecord> indexNoRecordMap = recordContainer.getIndexNoRecordMap();
        this.fileTag = (String) indexNoRecordMap.get(FILE_TAG_INDEX).getValue();
        this.suffix = (String) indexNoRecordMap.get(SUFFIX_INDEX).getValue();
        this.fileSize = (Long) indexNoRecordMap.get(FILE_SIZE_INDEX).getValue();
        this.fileStorageKey = (String) indexNoRecordMap.get(FILE_STORAGE_KEY_INDEX).getValue();
    }

    private void putFilePropertyToContainer(){
        ProtoRecord fileTagRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(FILE_TAG_INDEX, fileTag);
        recordContainer.putRecord(fileTagRecord);
        ProtoRecord suffixRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(SUFFIX_INDEX, suffix);
        recordContainer.putRecord(suffixRecord);
        ProtoRecord fileSizeRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(FILE_SIZE_INDEX, fileSize);
        recordContainer.putRecord(fileSizeRecord);
        ProtoRecord fileStorageKeyRecord = ProtoRecordFactory.buildProtoRecordByIndexAndValue(FILE_STORAGE_KEY_INDEX, fileStorageKey);
        recordContainer.putRecord(fileStorageKeyRecord);

        propertyValueContainer.put(FILE_TAG_FIELD, fileTag);
        propertyValueContainer.put(SUFFIX_FIELD, suffix);
        propertyValueContainer.put(FILE_SIZE_FIELD, fileSize);
        propertyValueContainer.put(FILE_STORAGE_KEY_FIELD, fileStorageKey);
    }

    private void assertFileExist(File file){
        if(Objects.isNull(file)){
            throw new SpObjectStoreException("file为空");
        }
        if(!file.exists()){
            throw new SpObjectStoreException("文件[%s]不存在", file.getAbsolutePath());
        }
    }

    private void assertNameIsFile(){
        if(!this.name.contains(".")){
            throw new SpObjectStoreException("name[%s]不是一个文件名", this.name);
        }
    }

    private void assertFileTagIsSupported(){
        if(StringUtils.isBlank(this.fileTag)){
            throw new SpException("SpFile保存文件未指定文件tag类型");
        }
        FileTag fileTagEnum = FileTag.toEnum(this.fileTag);
        if(FileTag.NONE.equals(fileTagEnum)){
            throw new SpException("SpFile保存文件类型不支持");
        }
    }

    private void assertParentObjectIsFolder(){
        if(Objects.nonNull(parentSysObject) && SystemTag.FILE.getValue().equals(parentSysObject.getSystemTag())){
            throw new SpException("SpFile的父对象不能是SpFile类型");
        }
    }

    private void uploadInputStreamIfChanged(){
        if(Objects.isNull(inputStream) || !isNeedUploadFile){
            return;
        }
        if(StringUtils.isNotBlank(fileStorageKey)){
            //若原来存在文件先删除旧文件
            session.fileStorage().removeObject(fileStorageKey);
        }
        assertIdNotNull(id);
        this.fileStorageKey = defaultFileStorageKey(type, id, name);
        session.fileStorage().setObject(fileStorageKey, inputStream);
        isNeedUploadFile = false;
    }

    private void removeFileStorage(){
        if(StringUtils.isNotBlank(fileStorageKey)){
            session.fileStorage().removeObject(fileStorageKey);
        }
    }

    /**
     * 设置文件类型
     *
     * @param fileTag 文件类型
     */
    @Override
    public void setFileTag(FileTag fileTag) {
        this.fileTag = fileTag.getValue();
    }

    @Override
    public void setStorage(File file) {
        assertFileExist(file);
        setStorage(InputStreamUtils.getByteArrayInputStreamFromFile(file));
    }

    @Override
    public void setStorage(String fileStorageKey) {
        this.fileStorageKey = fileStorageKey;
        this.inputStream = session.fileStorage().getObjectStream(fileStorageKey);
        this.fileSize = InputStreamUtils.getAvailable(inputStream);
        this.isNeedUploadFile = false;
    }

    @Override
    public void setStorage(InputStream inputStream) {
        this.inputStream = inputStream;
        this.fileSize = InputStreamUtils.getAvailable(inputStream);
        this.isNeedUploadFile = true;
    }

    /**
     * 获取文件大小
     *
     * @return 字节数
     */
    @Override
    public long getSize() {
        return fileSize;
    }

    /**
     * 获取文件后缀名
     *
     * @return 后缀名
     */
    @Override
    public String getSuffix() {
        return suffix;
    }

    /**
     * 获取存储路径key
     *
     * @return 存储路径
     */
    @Override
    public String getStorageKey() {
        return this.fileStorageKey;
    }

    /**
     * 获取文件流
     *
     * @return 文件流
     */
    @Override
    public InputStream getInputStream() {
        //若该对象没有获取流，先从存储中获取
        if(Objects.isNull(inputStream) && StringUtils.isNotBlank(fileStorageKey)){
            setStorage(fileStorageKey);
        }
        return this.inputStream;
    }

    /**
     * 下载文件到本地
     *
     * @param targetFilePath 本地文件路径
     * @return 下载后的文件
     */
    @Override
    public File download(String targetFilePath) {
        return session.fileStorage().downloadObject(fileStorageKey, targetFilePath);
    }

    /**
     * 获取文件预览链接
     *
     * @param expireSeconds 有效时间（秒）
     * @return 预览链接
     */
    @Override
    public String getPreviewUrl(int expireSeconds) {
        assertGetPreviewUrlFileStorageKeyNotBlank();
        return session.fileStorage().getObjectPreviewUrl(fileStorageKey, expireSeconds);
    }

    private void assertGetPreviewUrlFileStorageKeyNotBlank(){
        if(StringUtils.isBlank(fileStorageKey)){
            throw new SpFileStorageException("获取预览链接失败，fileStorageKey为空");
        }
    }

    private static String defaultFileStorageKey(String type, Long id, String name){
        return type + pathSeparator + id + pathSeparator + name;
    }
}
