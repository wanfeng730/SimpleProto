package cn.wanfeng.sp.api.mapper.search;

import cn.wanfeng.sp.api.dataobject.SpUserDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date: 2024-12-14 18:08
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public interface SpUserMapper extends BaseMapper<SpUserDO> {

    List<SpUserDO> findAll();

    /**
     * 动态查询
     *
     * @param queryWrapper 查询条件
     * @param iPage        分页信息
     * @return 结果
     */
    IPage<SpUserDO> findByWrapper(@Param(Constants.WRAPPER) QueryWrapper<SpUserDO> queryWrapper, IPage<SpUserDO> iPage);

    /**
     * 根据用户id查找
     * @param id 用户id
     * @return 用户
     */
    SpUserDO findById(@Param("id") Long id);

    /**
     * 根据用户名查找
     * @param username 根据用户名查找
     * @return 用户
     */
    SpUserDO findByUsername(@Param("username") String username);
}
