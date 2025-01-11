package cn.wanfeng.sp.api.service;


import cn.hutool.core.bean.BeanUtil;
import cn.wanfeng.sp.api.dataobject.SpUserDO;
import cn.wanfeng.sp.api.dataobject.SpUserDTO;
import cn.wanfeng.sp.api.mapper.search.SpUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @date: 2025-01-11 16:25
 * @author: luozh.wanfeng
 * @since: 1.1
 */
@Service
public class SpUserServiceImpl implements SpUserService{

    @Resource
    private SpUserMapper spUserMapper;

    /**
     * 获取用户详情
     *
     * @param id 用户id
     * @return 用户
     */
    @Override
    public SpUserDTO detail(Long id) {
        SpUserDO spUserDO = spUserMapper.findById(id);
        return BeanUtil.copyProperties(spUserDO, SpUserDTO.class);
    }
}
