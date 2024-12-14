package cn.wanfeng.sp.api.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @date: 2024-06-21 10:46
 * @author: luozh
 * @since: 1.0
 */
@Data
public class SpBaseObjectDO {

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
    /**
     * 十六进制形式的二进制数据
     */
    @TableField("data")
    private byte[] data;
}
