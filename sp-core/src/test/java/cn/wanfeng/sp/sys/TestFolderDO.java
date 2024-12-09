package cn.wanfeng.sp.sys;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 * @date: 2024-12-09 22:24
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public class TestFolderDO {

    @TableId("id")
    private Long id;

    @TableField("name")
    private String name;

    @TableField("type")
    private String type;

    @TableField("create_date")
    private Date createDate;

    @TableField("tag")
    private String tag;

    @TableField("path")
    private String path;

    @TableField("parent_id")
    private Long parentId;

    @TableField("parent_path")
    private String parentPath;

    @TableField("display_name")
    private String displayName;

    @TableField("code")
    private String code;

    @TableField("expire_date")
    private String expireDate;


}
