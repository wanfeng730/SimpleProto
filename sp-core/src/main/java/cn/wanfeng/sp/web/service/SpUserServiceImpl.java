package cn.wanfeng.sp.web.service;

import cn.hutool.core.bean.BeanUtil;
import cn.wanfeng.sp.api.dataobject.SpUserDO;
import cn.wanfeng.sp.api.dataobject.SpUserDTO;
import cn.wanfeng.sp.api.mapper.search.SpUserMapper;
import cn.wanfeng.sp.model.QueryParameter;
import cn.wanfeng.sp.model.ListResult;
import cn.wanfeng.sp.util.ObjectConvertUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 查询用户列表
     *
     * @param queryParameter 查询参数
     * @return 返回结果
     */
    @Override
    public ListResult<SpUserDTO> listUser(QueryParameter queryParameter) {
        QueryWrapper<SpUserDO> queryWrapper = queryParameter.toQueryWrapper();
        IPage<SpUserDO> iPage = queryParameter.toPage();

        IPage<SpUserDO> page = spUserMapper.findByWrapper(queryWrapper, iPage);
        List<SpUserDTO> spUserDTOList = ObjectConvertUtil.convertList(page.getRecords(), SpUserDTO.class);
        return ListResult.build(spUserDTOList, queryParameter.getPageInfo(), page.getTotal());
    }
}
