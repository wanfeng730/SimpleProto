package cn.wanfeng.sp.storage.mapper;

import cn.wanfeng.sp.base.object.SpBaseObjectDO;
import cn.wanfeng.sp.base.object.SpSettingsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @date: 2024-11-04 23:48
 * @author: luozh.wanfeng
 * @description: 数据库存储Mapper统一方法接口
 * @since: 1.0
 */
@Mapper
public interface DatabaseStorageMapper {

    /**
     * 根据设置名称查询
     *
     * @param tableName 设置表名
     * @param name      设置名称
     * @return 设置
     */
    SpSettingsDO findSettingsByName(@Param("tableName") String tableName, @Param("name") String name);

    /**
     * 新建对象数据
     *
     * @param tableName 对象数据表名
     * @param objectDO  对象数据
     * @return 更新行数
     */
    int insertObject(@Param("tableName") String tableName, @Param("objectDO") SpBaseObjectDO objectDO);

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
}
