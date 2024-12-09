package cn.wanfeng.sp.api.base.object;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @date: 2024-12-09 21:23
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Data
public class SpSysObjectDO {

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

    @TableField("tag")
    private String tag;

    @TableField("path")
    private String path;

    @TableField("parent_id")
    private Long parentId;

    @TableField("parent_path")
    private String parentPath;

    @TableField("data")
    private byte[] data;
}
