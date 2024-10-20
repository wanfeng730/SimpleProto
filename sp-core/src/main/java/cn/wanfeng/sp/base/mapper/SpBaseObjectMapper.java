package cn.wanfeng.sp.base.mapper;

import cn.wanfeng.sp.base.object.SpBaseObjectDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @date: 2024-06-21 10:40
 * @author: luozh
 * @since: 1.0
 */
@Mapper
public interface SpBaseObjectMapper extends BaseMapper<SpBaseObjectDO> {

    SpBaseObjectDO findById(@Param("id") Long id);

    int insert(SpBaseObjectDO spBaseObjectDO);

    int update(SpBaseObjectDO spBaseObjectDO);

    int updateById(SpBaseObjectDO spBaseObjectDO);

}
