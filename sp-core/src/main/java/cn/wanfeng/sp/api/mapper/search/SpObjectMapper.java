package cn.wanfeng.sp.api.mapper.search;


import cn.wanfeng.sp.api.dataobject.SpDataObjectDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date: 2024-12-22 23:19
 * @author: luozh.wanfeng
 * @description: 查询系统对象
 * @since: 1.0
 */
public interface SpObjectMapper {

    /**
     * 根据id查询
     */
    SpDataObjectDO findById(@Param("id") Long id);

    /**
     * 根据类型type查询
     */
    List<SpDataObjectDO> findByType(@Param("type") String type);

    /**
     * 根据名称查询
     */
    List<SpDataObjectDO> findByName(@Param("name") String name);

    /**
     * 根据父id查询
     */
    List<SpDataObjectDO> findByParentId(@Param("parentId") Long parentId);

    /**
     * 根据父路径模糊查询
     */
    List<SpDataObjectDO> findByParentPathLike(@Param("parentPath") String parentPath);

    /**
     * 根据父路径精确查询
     */
    List<SpDataObjectDO> findByParentPathEquals(@Param("parentPath") String parentPath);

}
