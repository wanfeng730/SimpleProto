package cn.wanfeng.sp.api.service;

import cn.wanfeng.sp.api.dataobject.SpUserDTO;
import cn.wanfeng.sp.model.QueryModel;
import cn.wanfeng.sp.model.QueryResult;

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

    /**
     * 查询用户列表
     *
     * @param queryModel 查询参数
     * @return 返回结果
     */
    QueryResult<SpUserDTO> listUser(QueryModel queryModel);

}
