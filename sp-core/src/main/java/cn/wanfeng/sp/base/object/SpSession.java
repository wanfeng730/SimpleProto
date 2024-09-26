package cn.wanfeng.sp.base.object;

import cn.wanfeng.sp.base.mapper.SpBaseObjectMapper;
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

    public SpBaseObjectMapper baseObjectStorage() {
        return baseObjectMapper;
    }
}
