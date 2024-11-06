package cn.wanfeng.sp.session;

import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.base.object.SpBaseObjectDO;
import cn.wanfeng.sp.base.object.SpSettingsDO;
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

    public DatabaseStorageMapper databaseStorage() {
        return databaseStorageMapper;
    }

    public SearchStorageClient searchStorage(){
        return this.searchStorageClient;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createObjectToStorage(SpBaseObjectDO baseObjectDO, SpSettingsDO settingsDO, Map<String, Object> fieldNameValueMap) throws Exception {
        int insertRows = databaseStorage().insertObject(SimpleProtoConfig.dataTable, baseObjectDO);
        LogUtils.debug("数据表[{}]新建行数: {}", SimpleProtoConfig.dataTable, insertRows);
        //更新设置表
        int updateRows = databaseStorage().updateSettings(SimpleProtoConfig.settingsTable, settingsDO);
        // 该对象保存到高级搜索存储
        searchStorage().insertObject(SimpleProtoConfig.dataTable, fieldNameValueMap);

    }
}
