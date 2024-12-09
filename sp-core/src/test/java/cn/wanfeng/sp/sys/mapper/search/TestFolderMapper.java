package cn.wanfeng.sp.sys.mapper.search;


import cn.wanfeng.sp.sys.TestFolderDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @date: 2024-12-09 22:23
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Mapper
public interface TestFolderMapper extends BaseMapper<TestFolderDO> {

    List<TestFolderDO> findAll();
}
