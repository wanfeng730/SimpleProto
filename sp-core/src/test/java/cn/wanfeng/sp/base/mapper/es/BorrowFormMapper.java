package cn.wanfeng.sp.base.mapper.es;

import cn.wanfeng.sp.base.BorrowFormDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date: 2024-11-22 00:26
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Mapper
public interface BorrowFormMapper extends BaseMapper<BorrowFormDO> {


    List<BorrowFormDO> findById(@Param("id") Long id);

    List<BorrowFormDO> findAll();

}
