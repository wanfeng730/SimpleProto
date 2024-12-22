package cn.wanfeng.sp.session;

import cn.wanfeng.sp.api.dataobject.SpBaseObjectDO;
import cn.wanfeng.sp.api.dataobject.SpSettingsDO;
import cn.wanfeng.sp.api.dataobject.SpSysObjectDO;
import cn.wanfeng.sp.cache.CacheOperator;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.storage.file.FileStorageClient;
import cn.wanfeng.sp.storage.mapper.postgres.DatabaseStorageMapper;
import cn.wanfeng.sp.storage.search.SearchStorageClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Objects;

/**
 * @date: 2024-05-06 11:14
 * @author: luozh
 * @description: simpleproto会话对象，保存数据库等操作组件
 * @since: 1.0
 */
@Component
public class SpSession {

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    @Resource
    private DatabaseStorageMapper databaseStorageMapper;

    @Resource
    private SearchStorageClient searchStorageClient;

    @Resource
    private CacheOperator cacheOperator;

    @Resource
    private FileStorageClient fileStorageClient;

    public DatabaseStorageMapper databaseStorage() {
        return this.databaseStorageMapper;
    }

    public SearchStorageClient searchStorage(){
        return this.searchStorageClient;
    }

    public CacheOperator cacheOperator(){
        return this.cacheOperator;
    }

    public FileStorageClient fileStorage(){
        return this.fileStorageClient;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createBaseObjectToStorage(SpBaseObjectDO baseObjectDO, Map<String, Object> fieldNameValueMap) throws Exception {
        //该对象保存到数据库存储
        databaseStorage().insertBaseObject(SimpleProtoConfig.dataTable, baseObjectDO);
        //该对象保存到高级搜索存储
        searchStorage().insertObject(SimpleProtoConfig.dataTable, fieldNameValueMap);
    }
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createSysObjectToStorage(SpSysObjectDO sysObjectDO, Map<String, Object> fieldNameValueMap) throws Exception {
        //该对象保存到数据库存储
        databaseStorage().insertSysObject(SimpleProtoConfig.dataTable, sysObjectDO);
        //该对象保存到高级搜索存储
        searchStorage().insertObject(SimpleProtoConfig.dataTable, fieldNameValueMap);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateBaseObjectToStorage(SpBaseObjectDO baseObjectDO, Map<String, Object> fieldNameValueMap) throws Exception{
        //更新数据库存储
        databaseStorage().updateBaseObject(SimpleProtoConfig.dataTable, baseObjectDO);
        //更新高级搜索存储
        searchStorage().updateObject(SimpleProtoConfig.dataTable, fieldNameValueMap);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateSysObjectToStorage(SpSysObjectDO sysObjectDO, Map<String, Object> fieldNameValueMap) throws Exception{
        //更新数据库存储
        databaseStorage().updateSysObject(SimpleProtoConfig.dataTable, sysObjectDO);
        //更新高级搜索存储
        searchStorage().updateObject(SimpleProtoConfig.dataTable, fieldNameValueMap);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void removeObjectFromStorage(Long id) throws Exception{
        //删除数据库存储
        databaseStorage().removeObject(SimpleProtoConfig.dataTable, id);
        //删除高级搜索存储
        searchStorage().removeObject(SimpleProtoConfig.dataTable, id);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Long increaseLongByName(@NotBlank String name){
        SpSettingsDO settings = databaseStorage().findSettingsByName(SimpleProtoConfig.settingsTable, name);
        if (Objects.isNull(settings)) {
            settings = new SpSettingsDO();
            settings.setName(name);
            settings.setIncreaseLong(1L);
            settings.setIncreaseString("");
            databaseStorage().insertSettings(SimpleProtoConfig.settingsTable, settings);
        }else {
            Long increaseLong = settings.getIncreaseLong();
            increaseLong++;
            settings.setIncreaseLong(increaseLong);
            databaseStorage().updateSettings(SimpleProtoConfig.settingsTable, settings);
        }
        return settings.getIncreaseLong();
    }
}
