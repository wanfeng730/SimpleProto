package cn.wanfeng.sp.api.model;

import lombok.Data;

/**
 * SpPropertyValue: 存储opensearch数据单个字段信息.
 *
 * @date: 2025-12-01 17:46
 * @author: luozh.wanfeng
 */
@Data
public class SpPropertyValue {

    private Class<?> clazz;

    private Object value;

    private SpPropertyValue(Class<?> clazz, Object value) {
        this.clazz = clazz;
        this.value = value;
    }

    public static SpPropertyValue build(Class<?> clazz, Object value){
        return new SpPropertyValue(clazz, value);
    }
}
