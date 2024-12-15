package cn.wanfeng.sp.api.domain;

import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.anno.Type;
import cn.wanfeng.sp.api.constant.SpObjectTypeConstants;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import lombok.Getter;

import java.util.Date;

/**
 * @date: 2024-12-14 15:43
 * @author: luozh.wanfeng
 * @description: 用户
 * @since: 1.0
 */
@Getter
@Type(SpObjectTypeConstants.USER)
public class SpUser extends SpBaseObject {

    /**
     * 用户名
     */
    @ProtoField(index = 1007, name = "username")
    private String username;
    /**
     * 密码
     */
    @ProtoField(index = 1008, name = "password")
    private String password;
    /**
     * 用户显示名（昵称）
     */
    @ProtoField(index = 1009, name = "display_name")
    private String displayName;
    /**
     * 到期时间
     */
    @ProtoField(index = 1010, name = "expire_date")
    private Date expireDate;

    public SpUser(SpSession session, String type, String username, String password, String displayName, Date expireDate) {
        super(session, type);
        this.displayName = displayName;
        this.expireDate = expireDate;
        this.password = password;
        this.username = username;
    }

    public SpUser(SpSession session, String username, String password, String displayName, Date expireDate) {
        super(session, SpObjectTypeConstants.USER);
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.expireDate = expireDate;
    }

    public SpUser(SpSession session, String type, String username) {
        super(session, type);
        this.username = username;
        this.password = SimpleProtoConfig.userDefaultPassword;
        this.displayName = username;
        this.expireDate = null;
    }

    public SpUser(SpSession session, String username) {
        super(session, SpObjectTypeConstants.USER);
        this.username = username;
        this.password = SimpleProtoConfig.userDefaultPassword;
        this.displayName = username;
        this.expireDate = null;
    }

    public SpUser(SpSession session, Long id) {
        super(session, id);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
