package cn.wanfeng.sp.api.dataobject;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @date: 2024-10-20 15:06
 * @author: luozh.wanfeng
 * @since: 1.0
 */
@Data
public class SpSettingsDO {

    @TableId("name")
    private String name;

    @TableField("increase_long")
    private Long increaseLong;

    @TableField("increase_string")
    private String increaseString;

    public SpSettingsDO() {
    }

    public SpSettingsDO(String name, Long increaseLong, String increaseString) {
        this.name = name;
        this.increaseLong = increaseLong;
        this.increaseString = increaseString;
    }
}
