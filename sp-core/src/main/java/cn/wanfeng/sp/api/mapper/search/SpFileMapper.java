package cn.wanfeng.sp.api.mapper.search;

import cn.wanfeng.sp.api.dataobject.SpFileDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date: 2024-12-22 23:44
 * @author: luozh.wanfeng
 * @description: 查询文件对象
 * @since: 1.0
 */
public interface SpFileMapper {

    /**
     * 根据id查询
     */
    SpFileDO findById(@Param("id") Long id);

    /**
     * 根据类型type查询
     */
    List<SpFileDO> findByType(@Param("type") String type);

    /**
     * 根据名称查询
     */
    List<SpFileDO> findByName(@Param("name") String name);

    /**
     * 根据父id查询
     */
    List<SpFileDO> findByParentId(@Param("parentId") Long parentId);

    /**
     * 根据父路径模糊查询
     */
    List<SpFileDO> findByParentPathLike(@Param("parentPath") String parentPath);

    /**
     * 根据父路径精确查询
     */
    List<SpFileDO> findByParentPathEquals(@Param("parentPath") String parentPath);

    /**
     * 根据付路径模糊查询，文件类型查询
     */
    List<SpFileDO> findByParentPathLikeAndFileTag(@Param("parentPath") String parentPath, @Param("fileTag") String fileTag);

}
