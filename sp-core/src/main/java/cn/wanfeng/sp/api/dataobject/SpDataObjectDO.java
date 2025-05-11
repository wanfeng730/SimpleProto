package cn.wanfeng.sp.api.dataobject;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @date: 2024-12-09 21:23
 * @author: luozh.wanfeng
 * @description: 数据库交互实体类
 * @since: 1.0
 *
 * 2025-05-11 sys对象和base对象统一使用该对象来保存数据库，减少代码维护成本
 */
@Data
public class SpDataObjectDO {

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
