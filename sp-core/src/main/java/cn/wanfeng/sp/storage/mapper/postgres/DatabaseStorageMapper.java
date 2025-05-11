package cn.wanfeng.sp.storage.mapper.postgres;

import cn.wanfeng.sp.api.dataobject.SpDataObjectDO;
import cn.wanfeng.sp.api.dataobject.SpSettingsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date: 2024-11-04 23:48
 * @author: luozh.wanfeng
 * @description: 数据库存储Mapper统一方法接口
 * @since: 1.0
 */
@Mapper
public interface DatabaseStorageMapper {

    /**
     * 创建对象
     * @param tableName 表名
     * @param dataObjectDO 对象
     * @return 新建行数
     */
    int insertObject(@Param("tableName") String tableName, @Param("dataObjectDO") SpDataObjectDO dataObjectDO);

    /**
     * 批量创建对象
     * @param tableName 表名
     * @param dataObjectDOList 系统对象列表
     * @return 新建行数
     */
    int batchInsertObject(@Param("tableName") String tableName, @Param("dataObjectDOList") List<SpDataObjectDO> dataObjectDOList);

    /**
     * 更新对象
     * @param tableName 表名
     * @param dataObjectDO 对象
     * @return 更新行数
     */
    int updateObject(@Param("tableName") String tableName, @Param("dataObjectDO") SpDataObjectDO dataObjectDO);

    /**
     * 批量更新对象
     * @param tableName 表名
     * @param dataObjectDOList 对象列表
     * @return 更新行数
     */
    int batchUpdateObject(@Param("tableName") String tableName, @Param("dataObjectDOList") List<SpDataObjectDO> dataObjectDOList);

    /**
     * 根据对象id删除
     * @param tableName 对象数据表名
     * @param id 对象id
     * @return 删除行数
     */
    int removeObject(@Param("tableName") String tableName, @Param("id") Long id);

    /**
     * 根据对象id查询
     * @param tableName 对象数据表名
     * @param id 对象id
     * @return 对象数据
     */
    SpDataObjectDO findObjectById(@Param("tableName") String tableName, @Param("id") Long id);

    /**
     * 根据路径查询对象
     * @param path 路径
     * @return 对象
     */
    SpDataObjectDO findObjectByPath(@Param("tableName") String tableName, @Param("path") String path);

    /**
     * 根据路径模糊查询对象
     * @param tableName 表名
     * @param path 路径
     * @return 对象列表
     */
    List<SpDataObjectDO> findObjectByLikePath(@Param("tableName") String tableName, @Param("path") String path);

    /**
     * 根据路径模糊查询指定类型子对象
     * @param tableName 表名
     * @param path 路径
     * @param tag 类型
     * @return 对象列表
     */
    Integer countObjectByLikePathAndTag(@Param("tableName") String tableName, @Param("path") String path, @Param("tag") String tag);

    /**
     * 根据设置名称查询
     *
     * @param tableName 设置表名
     * @param name      设置名称
     * @return 设置
     */
    SpSettingsDO findSettingsByName(@Param("tableName") String tableName, @Param("name") String name);

    /**
     * 新建设置
     *
     * @param tableName  设置表名
     * @param settingsDO 设置
     * @return 更新行数
     */
    int insertSettings(@Param("tableName") String tableName, @Param("settingsDO") SpSettingsDO settingsDO);

    /**
     * 更新设置
     *
     * @param tableName  设置表名
     * @param settingsDO 设置
     * @return 更新行数
     */
    int updateSettings(@Param("tableName") String tableName, @Param("settingsDO") SpSettingsDO settingsDO);

    /**
     * 查询所有数据表
     * @param currentScheme 模式名
     * @return 表名
     */
    List<String> listAllTable(@Param("currentScheme") String currentScheme);

    /**
     * 创建对象数据表
     * @param tableName 对象数据表名
     */
    void createDataTable(@Param("tableName") String tableName);

    /**
     * 创建设置表
     * @param tableName 设置表名
     */
    void createSettingsTable(@Param("tableName") String tableName);

    /**
     * 初始化设置表的数据
     * @param tableName 设置表名
     */
    void initSettingsTableData(@Param("tableName") String tableName);
}
