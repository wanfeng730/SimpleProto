package cn.wanfeng.sp.model;


import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.util.LogUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

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
    public static <T> ListResult<T> build(List<T> dataList, PageInfo pageInfo, long totalCount) {
        pageInfo.setTotalCount((int) totalCount);

        ListResult<T> result = new ListResult<>();
        result.setDataList(dataList);
        result.setPageInfo(pageInfo);
        return result;
    }

    /**
     * 构建查询结果
     * @param dataList 结果列表
     * @param queryParameter 查询参数
     * @param totalCount 总数
     * @return ListResult
     * @param <T> 数据模型
     */
    public static <T> ListResult<T> build(List<T> dataList, QueryParameter queryParameter, long totalCount){
        if(Objects.isNull(queryParameter)){
            throw new SpException("构造ListResult失败，查询参数queryParameter为空");
        }
        if(Objects.isNull(queryParameter.getPageInfo())){
            LogUtil.warn("构造ListResult时的queryParameter分页信息为空");
        }
        return build(dataList, queryParameter.getPageInfo(), totalCount);
    }
}
