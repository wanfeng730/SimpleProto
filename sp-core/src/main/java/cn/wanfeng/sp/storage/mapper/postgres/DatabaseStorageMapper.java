package cn.wanfeng.sp.storage.mapper.postgres;

import cn.wanfeng.sp.base.object.SpBaseObjectDO;
import cn.wanfeng.sp.base.object.SpSettingsDO;
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
     * 根据对象id查询
     * @param tableName 对象数据表名
     * @param id 对象id
     * @return 对象数据
     */
    SpBaseObjectDO findObjectById(@Param("tableName") String tableName, @Param("id") Long id);

    /**
     * 根据对象id更新
     * @param tableName 对象数据表名
     * @param objectDO 对象数据
     * @return 更新行数
     */
    int updateObject(@Param("tableName") String tableName, @Param("objectDO") SpBaseObjectDO objectDO);

    /**
     * 新建对象数据
     *
     * @param tableName 对象数据表名
     * @param objectDO  对象数据
     * @return 更新行数
     */
    int insertObject(@Param("tableName") String tableName, @Param("objectDO") SpBaseObjectDO objectDO);

    /**
     * 根据对象id删除
     * @param tableName 对象数据表名
     * @param id 对象id
     * @return 删除行数
     */
    int removeObject(@Param("tableName") String tableName, @Param("id") Long id);

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
