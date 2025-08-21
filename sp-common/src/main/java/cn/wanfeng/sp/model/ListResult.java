package cn.wanfeng.sp.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * QueryResult: desc.
 *
 * @date: 2025-05-17 17:32
 * @author: luozh.wanfeng
 */
@Data
public class ListResult<T> {

    @Schema(description = "分页信息")
    private PageInfo pageInfo;

    @Schema(description = "数据列表")
    private List<T> dataList;

    public ListResult() {
    }

    /**
     * 构建查询结果
     *
     * @param dataList   分页查询结果
     * @param pageInfo   分页信息
     * @param totalCount 分页查询总数
     * @param <T>        数据模型
     * @return 查询结果
     */
    public static <T> ListResult<T> build(List<T> dataList, PageInfo pageInfo, Long totalCount) {
        pageInfo.setTotalCount(totalCount);

        ListResult<T> result = new ListResult<>();
        result.setDataList(dataList);
        result.setPageInfo(pageInfo);
        return result;
    }
}
