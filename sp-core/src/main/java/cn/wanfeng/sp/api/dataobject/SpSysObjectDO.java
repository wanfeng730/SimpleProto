package cn.wanfeng.sp.api.dataobject;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @date: 2024-12-09 21:23
 * @author: luozh.wanfeng
 * @description: 系统对象数据库交互实体类
 * @since: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpSysObjectDO extends SpBaseObjectDO {

    // TableField From SpBaseObjectDO:

    // @TableId("id")
    // private Long id;
    //
    // @TableField("type")
    // private String type;
    //
    // @TableField("name")
    // private String name;
    //
    // @TableField("create_date")
    // private Date createDate;
    //
    // @TableField("modify_date")
    // private Date modifyDate;
    //
    // @TableField("is_delete")
    // private Boolean isDelete;
    //
    // @TableField("data")
    // private byte[] data;

    @TableField("tag")
    private String tag;

    @TableField("path")
    private String path;

    @TableField("parent_id")
    private Long parentId;

    @TableField("parent_path")
    private String parentPath;


}
