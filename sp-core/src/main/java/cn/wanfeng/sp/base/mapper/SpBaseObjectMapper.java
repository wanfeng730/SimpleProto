package cn.wanfeng.sp.base.mapper;

import cn.wanfeng.sp.base.object.SpBaseObjectDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @date: 2024-06-21 10:40
 * @author: luozh
 * @since: 1.0
 */
@Mapper
public interface SpBaseObjectMapper extends BaseMapper<SpBaseObjectDO> {

    SpBaseObjectDO findById(@Param("id") Long id);

    @Update("UPDATE sp_object_table  " +
            "SET type=#{type}, name=#{name}, create_date=#{createDate}, modify_date=#{modifyDate}, is_delete=#{isDelete}, data=#{data, jdbcType=BLOB} " +
            "WHERE id=#{id}")
    int updateById(SpBaseObjectDO spBaseObjectDO);
}
