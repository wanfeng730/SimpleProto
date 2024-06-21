package cn.wanfeng.sp.base.object;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @date: 2024-06-21 10:46
 * @author: luozh
 * @since: 1.0
 */
@Data
@TableName(autoResultMap = true)
public class SpBaseObjectDO {
    private Long id;
    private String type;
    private String name;
    private Date createDate;
    private Date modifyDate;
    private Boolean isDelete;
    private byte[] data;
}
