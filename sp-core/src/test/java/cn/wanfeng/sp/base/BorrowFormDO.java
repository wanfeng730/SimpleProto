package cn.wanfeng.sp.base;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @date: 2024-11-22 00:27
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Data
@TableName(autoResultMap = true)
public class BorrowFormDO {

    @TableId("id")
    private Long id;

    @TableField("name")
    private String name;

    @TableField("form_no")
    private String formNo;

    @TableField("create_date")
    private Date createDate;
}
