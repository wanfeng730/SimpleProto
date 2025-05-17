package cn.wanfeng.sp.model;


import cn.wanfeng.sp.enums.QueryFieldType;
import cn.wanfeng.sp.enums.QueryOperator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * FilterColumn: 过滤字段.
 *
 * @date: 2025-05-17 15:30
 * @author: luozh.wanfeng
 */
@Data
public class FilterColumn {

    @Schema(description = "字段名")
    private String fieldName;

    /**
     * @see cn.wanfeng.sp.enums.QueryFieldType
     */
    @Schema(description = "字段类型")
    private String fieldType;

    /**
     * @see cn.wanfeng.sp.enums.QueryOperator
     */
    @Schema(description = "查询操作")
    private Integer operate;

    @Schema(description = "查询值")
    private String value;

    public FilterColumn() {
    }

    public FilterColumn(String fieldName, QueryFieldType queryFieldType, QueryOperator queryOperator, String value) {
        this.fieldName = fieldName;
        this.fieldType = queryFieldType.getValue();
        this.operate = queryOperator.getValue();
        this.value = value;
    }
}
