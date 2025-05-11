package cn.wanfeng.sp.api.dataobject;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @date: 2024-12-22 22:35
 * @author: luozh.wanfeng
 * @description: 文件夹对象数据库实体类
 * @since: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpFolderDO extends SpDataObjectDO {

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

    @TableField("data")
    private byte[] data;

    @TableField("tag")
    private String tag;

    @TableField("path")
    private String path;

    @TableField("parent_id")
    private Long parentId;

    @TableField("parent_path")
    private String parentPath;
}
