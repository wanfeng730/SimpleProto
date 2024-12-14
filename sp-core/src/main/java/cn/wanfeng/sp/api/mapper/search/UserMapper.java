package cn.wanfeng.sp.api.mapper.search;

import cn.wanfeng.sp.api.dataobject.SpUserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @date: 2024-12-14 18:08
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public interface UserMapper extends BaseMapper<SpUserDO> {

    /**
     * 根据用户名查找
     * @param username 根据用户名查找
     * @return 用户
     */
    SpUserDO findByUsername(@Param("username") String username);
}
