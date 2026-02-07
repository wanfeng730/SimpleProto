package cn.wanfeng.sp.storage.search;

import cn.wanfeng.sp.api.domain.ISpBaseObject;
import cn.wanfeng.sp.api.model.SpPropertyValue;
import cn.wanfeng.sp.elastic.ElasticDateTimePattern;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @date: 2024-11-06 21:28
 * @author: luozh.wanfeng
 * @description: 高级搜索存储
 * @since: 1.0
 */
public interface SearchStorageClient {

    String OBJECT_ID_KEY = ISpBaseObject.ID_FIELD;

    String DEFAULT_DATE_FORMAT = ElasticDateTimePattern.DATE_TIME_MILLIS.toPattern() + "||" +ElasticDateTimePattern.EPOCH_SECOND.toPattern();

    void createIndexIfNotExist(String tableName);

    /**
     * 新建对象数据
     * @param tableName 对象数据表名、索引
     * @param propertyValueContainer 对象数据
     */
    void insertObject(String tableName, Map<String, SpPropertyValue> propertyValueContainer) throws Exception;

    /**
     * 新建对象数据
     * @param tableName 对象数据表名、索引
     * @param propertyValueContainer 对象数据
     */
    void insertObject(String tableName, Map<String, SpPropertyValue> propertyValueContainer, boolean checkMapping) throws Exception;

    /**
     * 更新对象数据
     * @param tableName 对象数据表名、索引
     * @param propertyValueContainer 对象数据
     */
    void updateObject(String tableName, Map<String, SpPropertyValue> propertyValueContainer) throws Exception;

    /**
     * 删除对象数据
     * @param tableName 对象数据表名、索引
     * @param id 对象id
     */
    void removeObject(String tableName, Long id) throws Exception;

    /**
     * 批量新建对象数据
     * @param tableName 对象数据表名、索引
     * @param propertyValueContainerList 对象数据列表
     */
    void bulkInsertObject(String tableName, List<Map<String, SpPropertyValue>> propertyValueContainerList) throws IOException;

    /**
     * 批量更新对象数据
     * @param tableName 对象数据表名、索引
     * @param propertyValueContainerList 对象数据列表
     */
    void bulkUpdateObject(String tableName, List<Map<String, SpPropertyValue>> propertyValueContainerList) throws IOException;

    /**
     * 批量删除对象数据
     * @param tableName 对象数据表名、索引
     * @param idList 对象id列表
     */
    void bulkRemoveObject(String tableName, List<Long> idList) throws IOException;

    /**
     * 将某个类中的ProtoField注解字段的类型同步更到mapping
     * @param tableName 对象数据表名、索引
     * @param clazz 类
     */
    void syncProtoFieldToMapping(String tableName, Class<? extends ISpBaseObject> clazz);

    /**
     * 将某个类中的TableField、TableId注解字段同步到mapping
     * @param tableName 对象数据表名、索引
     * @param clazz 类
     * @param updateMappingCache 是否在同步完mapping后更新到本地缓存
     */
    void syncTableFieldToMapping(String tableName, Class<?> clazz, boolean updateMappingCache);
}
