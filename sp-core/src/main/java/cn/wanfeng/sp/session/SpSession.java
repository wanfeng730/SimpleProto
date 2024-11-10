package cn.wanfeng.sp.session;

import cn.wanfeng.sp.base.object.SpBaseObjectDO;
import cn.wanfeng.sp.cache.CacheOperator;
import cn.wanfeng.sp.config.SimpleProtoConfig;
import cn.wanfeng.sp.storage.mapper.DatabaseStorageMapper;
import cn.wanfeng.sp.storage.search.SearchStorageClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @date: 2024-05-06 11:14
 * @author: luozh
 * @description: simpleproto会话对象，保存数据库等操作组件
 * @since: 1.0
 */
@Component
public class SpSession {

    @Resource
    private DatabaseStorageMapper databaseStorageMapper;

    @Resource
    private SearchStorageClient searchStorageClient;

    @Resource
    private CacheOperator cacheOperator;

    public DatabaseStorageMapper databaseStorage() {
        return this.databaseStorageMapper;
    }

    public SearchStorageClient searchStorage(){
        return this.searchStorageClient;
    }

    public CacheOperator cacheOperator(){
        return this.cacheOperator;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createObjectToStorage(SpBaseObjectDO baseObjectDO, Map<String, Object> fieldNameValueMap) throws Exception {
        //该对象保存到数据库存储
        databaseStorage().insertObject(SimpleProtoConfig.dataTable, baseObjectDO);
        //该对象保存到高级搜索存储
        searchStorage().insertObject(SimpleProtoConfig.dataTable, fieldNameValueMap);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateObjectToStorage(SpBaseObjectDO baseObjectDO, Map<String, Object> fieldNameValueMap) throws Exception{
        //更新数据库存储
        databaseStorage().updateObject(SimpleProtoConfig.dataTable, baseObjectDO);
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
}
