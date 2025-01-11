package cn.wanfeng.sp.api.dataobject;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @date: 2025-01-11 16:22
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Data
public class SpUserDTO {

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
