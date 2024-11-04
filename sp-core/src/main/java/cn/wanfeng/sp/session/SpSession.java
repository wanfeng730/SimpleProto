package cn.wanfeng.sp.session;

import cn.wanfeng.sp.base.mapper.SpBaseObjectMapper;
import cn.wanfeng.sp.base.mapper.SpSettingsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @date: 2024-05-06 11:14
 * @author: luozh
 * @description: simpleproto会话对象，保存数据库等操作组件
 * @since: 1.0
 */
@Component
public class SpSession {

    @Resource
    private SpBaseObjectMapper baseObjectMapper;

    @Resource
    private SpSettingsMapper spSettingsMapper;

    public SpBaseObjectMapper objectStorage() {
        return baseObjectMapper;
    }

    public SpSettingsMapper settingsStorage() {
        return spSettingsMapper;
    }
}
