package cn.wanfeng.sp.storage.search;

import java.util.Map;

/**
 * @date: 2024-11-06 21:28
 * @author: luozh.wanfeng
 * @description: 高级搜索存储
 * @since: 1.0
 */
public interface SearchStorageClient {

    String OBJECT_ID_KEY = "id";

    /**
     * 新建对象数据
     * @param tableName 对象数据表名、索引
     * @param objectData 对象数据
     */
    void insertObject(String tableName, Map<String, Object> objectData) throws Exception;

    /**
     * 更新对象数据
     * @param tableName 对象数据表名、索引
     * @param objectData 对象数据
     */
    void updateObject(String tableName, Map<String, Object> objectData) throws Exception;

    /**
     * 删除对象数据
     * @param tableName 对象数据表名、索引
     * @param id 对象id
     */
    void removeObject(String tableName, Long id) throws Exception;
}
