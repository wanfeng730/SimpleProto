package cn.wanfeng.sp.base.mapper;

import cn.wanfeng.sp.base.object.SpSettingsDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @date: 2024-10-20 15:08
 * @author: luozh.wanfeng
 * @description: 设置项mapper
 * @since: 1.0
 */
@Mapper
public interface SpSettingsMapper extends BaseMapper<SpSettingsDO> {

    SpSettingsDO findByName(String name);
}
