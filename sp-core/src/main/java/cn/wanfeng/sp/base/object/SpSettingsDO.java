package cn.wanfeng.sp.base.object;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @date: 2024-10-20 15:06
 * @author: luozh.wanfeng
 * @since: 1.0
 */
@Data
@TableName(value = "sp_settings", autoResultMap = true)
public class SpSettingsDO {

    @TableId("name")
    private String name;

    @TableField("increase_long")
    private Long increaseLong;

    @TableField("increase_string")
    private String increaseString;
}
