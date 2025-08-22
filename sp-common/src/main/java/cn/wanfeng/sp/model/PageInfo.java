package cn.wanfeng.sp.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * PageInfo: 分页信息.
 *
 * @date: 2025-05-17 15:27
 * @author: luozh.wanfeng
 */
@Data
public class PageInfo {

    @Schema(description = "当前页")
    private Integer currentPage;

    @Schema(description = "每页数量")
    private Integer pageSize;

    @Schema(description = "总数")
    private Integer totalCount;

    public PageInfo() {
    }

    public PageInfo(Integer currentPage, Integer pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = 0;
    }

    public void setDefaultCurrentPage() {
        this.currentPage = 1;
    }

    public void setDefaultPageSize() {
        this.pageSize = 200;
    }
}
