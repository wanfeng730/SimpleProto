package cn.wanfeng.sp.api.mapper.search;


import cn.wanfeng.sp.api.dataobject.SpBaseObjectDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date: 2024-12-22 23:11
 * @author: luozh.wanfeng
 * @since: 1.0
 */
public interface SpBaseObjectMapper {

    /**
     * 根据id查询
     */
    SpBaseObjectDO findById(@Param("id") Long id);

    /**
     * 根据类型type查询
     */
    List<SpBaseObjectDO> findByType(@Param("type") String type);

    /**
     * 根据名称查询
     */
    List<SpBaseObjectDO> findByName(@Param("name") String name);

}
