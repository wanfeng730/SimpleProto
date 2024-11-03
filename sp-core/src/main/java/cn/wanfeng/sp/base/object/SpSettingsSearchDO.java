package cn.wanfeng.sp.base.object;


import lombok.Data;

/**
 * @date: 2024-11-03 22:29
 * @author: luozh.wanfeng
 * @description: simpleproto设置项es对象
 * @since: 1.0
 */
@Data
public class SpSettingsSearchDO {

    private String name;

    private Long increase_long;

    private String increase_string;

    public SpSettingsSearchDO() {
    }

    public SpSettingsSearchDO(String name, Long increase_long, String increase_string) {
        this.name = name;
        this.increase_long = increase_long;
        this.increase_string = increase_string;
    }
}
