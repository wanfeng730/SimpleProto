package cn.wanfeng.sp.api.mapper.search;

import cn.wanfeng.sp.api.dataobject.SpFolderDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date: 2024-12-22 23:32
 * @author: luozh.wanfeng
 * @description: 查询文件夹对象
 * @since: 1.0
 */
public interface SpFolderMapper {

    /**
     * 根据id查询
     */
    SpFolderDO findById(@Param("id") Long id);

    /**
     * 根据类型type查询
     */
    List<SpFolderDO> findByType(@Param("type") String type);

    /**
     * 根据名称查询
     */
    List<SpFolderDO> findByName(@Param("name") String name);

    /**
     * 根据父id查询
     */
    List<SpFolderDO> findByParentId(@Param("parentId") Long parentId);

    /**
     * 根据父路径模糊查询
     */
    List<SpFolderDO> findByParentPathLike(@Param("parentPath") String parentPath);

    /**
     * 根据父路径精确查询
     */
    List<SpFolderDO> findByParentPathEquals(@Param("parentPath") String parentPath);

}
