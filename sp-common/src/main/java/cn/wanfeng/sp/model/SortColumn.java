package cn.wanfeng.sp.model;


import cn.wanfeng.sp.enums.QuerySortType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * SortColumn: desc.
 *
 * @date: 2025-05-17 15:51
 * @author: luozh.wanfeng
 */
@Data
public class SortColumn {

    @Schema(name = "字段名")
    private String fieldName;

    @Schema(name = "排序方式")
    private String sortType;

    public SortColumn() {
    }

    public SortColumn(String fieldName, QuerySortType querySortType) {
        this.fieldName = fieldName;
        this.sortType = querySortType.getValue();
    }
}
