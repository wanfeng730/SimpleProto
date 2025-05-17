package cn.wanfeng.sp.api.service;

import cn.wanfeng.sp.api.dataobject.SpUserDTO;

/**
 * @date: 2025-01-11 16:24
 * @author: luozh.wanfeng
 * @since: 1.1
 */
public interface SpUserService {

    /**
     * 获取用户详情
     * @param id 用户id
     * @return 用户
     */
    SpUserDTO detail(Long id);

    SpUserDTO listUser()

}
