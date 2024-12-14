package cn.wanfeng.sp.api.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @date: 2024-12-14 18:09
 * @author: luozh.wanfeng
 * @since: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpUserDO extends SpBaseObjectDO{

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("display_name")
    private String displayName;

    @TableField("expire_date")
    private Date expireDate;
}
