package cn.wanfeng.sp.session;

import cn.wanfeng.sp.api.dataobject.SpDataObjectDO;
import cn.wanfeng.sp.api.dataobject.SpSettingsDO;
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
import java.io.IOException;
import java.util.List;
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
    public void createObjectToStorage(SpDataObjectDO dataObjectDO, Map<String, Object> documentList) throws Exception {
        //该对象保存到数据库存储
        databaseStorageMapper.insertObject(SimpleProtoConfig.dataTable, dataObjectDO);
        //该对象保存到高级搜索存储
        searchStorageClient.insertObject(SimpleProtoConfig.dataTable, documentList);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateObjectToStorage(SpDataObjectDO sysObjectDO, Map<String, Object> documentList) throws Exception{
        //更新数据库存储
        databaseStorageMapper.updateObject(SimpleProtoConfig.dataTable, sysObjectDO);
        //更新高级搜索存储
        searchStorageClient.updateObject(SimpleProtoConfig.dataTable, documentList);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void removeObjectFromStorage(Long id) throws Exception{
        //删除数据库存储
        databaseStorageMapper.removeObject(SimpleProtoConfig.dataTable, id);
        //删除高级搜索存储
        searchStorageClient.removeObject(SimpleProtoConfig.dataTable, id);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Long increaseLongByName(@NotBlank String name){
        SpSettingsDO settings = databaseStorageMapper.findSettingsByName(SimpleProtoConfig.settingsTable, name);
        if (Objects.isNull(settings)) {
            settings = new SpSettingsDO();
            settings.setName(name);
            settings.setIncreaseLong(1L);
            settings.setIncreaseString("");
            databaseStorageMapper.insertSettings(SimpleProtoConfig.settingsTable, settings);
        }else {
            Long increaseLong = settings.getIncreaseLong();
            increaseLong++;
            settings.setIncreaseLong(increaseLong);
            databaseStorageMapper.updateSettings(SimpleProtoConfig.settingsTable, settings);
        }
        return settings.getIncreaseLong();
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void bulkCreateObjectToStorage(List<SpDataObjectDO> dataObjectDOList, List<Map<String, Object>> documentList) throws IOException {
        databaseStorageMapper.batchInsertObject(SimpleProtoConfig.dataTable, dataObjectDOList);
        searchStorageClient.bulkInsertObject(SimpleProtoConfig.dataTable, documentList);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void bulkUpdateObjectToStorage(List<SpDataObjectDO> dataObjectDOList, List<Map<String, Object>> documentList) throws IOException {
        databaseStorageMapper.batchUpdateObject(SimpleProtoConfig.dataTable, dataObjectDOList);
        searchStorageClient.bulkUpdateObject(SimpleProtoConfig.dataTable, documentList);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void bulkRemoveObjectToStorage(List<Long> idList) throws IOException {
        databaseStorageMapper.batchRemoveObject(SimpleProtoConfig.dataTable, idList);
        searchStorageClient.bulkRemoveObject(SimpleProtoConfig.dataTable, idList);
    }

}
