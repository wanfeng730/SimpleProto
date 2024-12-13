package cn.wanfeng.sp.storage.mapper.postgres;

import cn.wanfeng.sp.api.base.object.SpBaseObjectDO;
import cn.wanfeng.sp.api.base.object.SpSettingsDO;
import cn.wanfeng.sp.api.sys.object.SpSysObjectDO;
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
    int updateBaseObject(@Param("tableName") String tableName, @Param("objectDO") SpBaseObjectDO objectDO);

    /**
     * 新建对象数据
     *
     * @param tableName 对象数据表名
     * @param objectDO  对象数据
     * @return 更新行数
     */
    int insertBaseObject(@Param("tableName") String tableName, @Param("objectDO") SpBaseObjectDO objectDO);

    /**
     * 根据对象id删除
     * @param tableName 对象数据表名
     * @param id 对象id
     * @return 删除行数
     */
    int removeObject(@Param("tableName") String tableName, @Param("id") Long id);

    /**
     * 根据id查询系统对象
     * @param id 系统对象id
     * @return 系统对象
     */
    SpSysObjectDO findSysObjectById(@Param("tableName") String tableName, @Param("id") Long id);

    /**
     * 根据路径查询系统对象
     * @param path 路径
     * @return 系统对象
     */
    SpSysObjectDO findSysObjectByPath(@Param("tableName") String tableName, @Param("path") String path);

    /**
     * 根据路径模糊查询系统对象
     * @param tableName 表名
     * @param path 路径
     * @return 系统对象列表
     */
    List<SpSysObjectDO> findSysObjectByLikePath(@Param("tableName") String tableName, @Param("path") String path);

    /**
     * 创建系统对象
     * @param tableName 表名
     * @param sysObjectDO 系统对象
     * @return 新建行数
     */
    int insertSysObject(@Param("tableName") String tableName, @Param("sysObjectDO") SpSysObjectDO sysObjectDO);

    /**
     * 更新系统对象
     * @param tableName 表名
     * @param sysObjectDO 系统对象
     * @return 更新行数
     */
    int updateSysObject(@Param("tableName") String tableName, @Param("sysObjectDO") SpSysObjectDO sysObjectDO);

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
