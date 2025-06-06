package cn.wanfeng.sp.api.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @date: 2024-12-14 18:09
 * @author: luozh.wanfeng
 * @since: 1.0
 */
@Data
public class SpUserDO {

    @TableId("id")
    private Long id;

    @TableField("type")
    private String type;

    @TableField("name")
    private String name;

    @TableField("create_date")
    private Date createDate;

    @TableField("modify_date")
    private Date modifyDate;

    @TableField("is_delete")
    private Boolean isDelete;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("display_name")
    private String displayName;

    @TableField("expire_date")
    private Date expireDate;
}
