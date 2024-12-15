package cn.wanfeng.sp.api.domain;


import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.anno.Type;
import cn.wanfeng.sp.session.SpSession;
import lombok.Getter;

/**
 * @date: 2024-12-15 22:22
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Getter
@Type("default_user")
public class DefaultUser extends SpUser{

    @ProtoField(index = 1, name = "description")
    private String description;

    public DefaultUser(SpSession session, String username, String description) {
        super(session, "default_user", username);
        this.description = description;
    }

    public DefaultUser(SpSession session, Long id) {
        super(session, id);
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
