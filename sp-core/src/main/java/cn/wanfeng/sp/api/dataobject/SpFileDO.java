package cn.wanfeng.sp.api.dataobject;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @date: 2024-12-22 23:42
 * @author: luozh.wanfeng
 * @description: 文件对象数据库交互实体类
 * @since: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpFileDO extends SpDataObjectDO {

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

    @TableField("file_tag")
    private String fileTag;

    @TableField("suffix")
    private String suffix;

    @TableField("file_size")
    private Long fileSize;

    @TableField("file_storage_key")
    private String fileStorageKey;


}
