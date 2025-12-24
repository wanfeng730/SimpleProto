package cn.wanfeng.sp.model;


import cn.wanfeng.sp.enums.QueryFieldType;
import cn.wanfeng.sp.enums.QueryOperator;
import cn.wanfeng.sp.enums.QuerySortType;
import cn.wanfeng.sp.exception.SimpleExceptionCode;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.util.LogUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * QueryModel: 前端查询参数模型.
 *
 * @date: 2025-05-17 15:21
 * @author: luozh.wanfeng
 */
@Data
public class QueryParameter {

    @Schema(description = "过滤字段列表")
    private List<FilterColumn> filterList;

    @Schema(description = "分页信息")
    @NotNull
    private PageInfo pageInfo;

    @Schema(description = "排序字段列表")
    private List<SortColumn> sortList;

    public QueryParameter() {
    }

    public QueryParameter(List<FilterColumn> filterList, PageInfo pageInfo, List<SortColumn> sortList) {
        this.filterList = filterList;
        this.pageInfo = pageInfo;
        this.sortList = sortList;
    }

    public void addFilterColumn(FilterColumn filterColumn) {
        assertFilterListNotNull();
        filterList.add(filterColumn);
    }

    public void updatePageInfo(Integer currentPage, Integer pageSize) {
        assertPageInfoNotNull();
        pageInfo.setCurrentPage(currentPage);
        pageInfo.setPageSize(pageSize);
    }

    public void updateTotalCount(Integer totalCount) {
        assertPageInfoNotNull();
        pageInfo.setTotalCount(totalCount);
    }

    public void assertFilterListNotNull() {
        if (Objects.isNull(filterList)) {
            filterList = new ArrayList<>();
        }
    }

    public void assertPageInfoNotNull() {
        if (Objects.isNull(pageInfo)) {
            pageInfo = new PageInfo();
        }
    }

    public void assertSortListNotNull() {
        if (Objects.isNull(sortList)) {
            sortList = new ArrayList<>();
        }
    }

    private static final String BETWEEN_SEPARATOR = "~";
    private static final String IN_SEPARATOR = ",";

    public <M> QueryWrapper<M> toQueryWrapper() {
        QueryWrapper<M> queryWrapper = new QueryWrapper<>();
        resolveFilterList(queryWrapper);
        resolveSortList(queryWrapper);
        queryWrapper.checkSqlInjection();
        return queryWrapper;
    }

    public <M> IPage<M> toPage() {
        assertPageInfoNotNull();
        assertPageInfoHasValue(pageInfo);
        return new Page<>(pageInfo.getCurrentPage(), pageInfo.getPageSize());
    }

    private <M> void resolveFilterList(QueryWrapper<M> queryWrapper) {
        assertFilterListNotNull();
        for (FilterColumn filterColumn : filterList) {
            validateFilterColumn(filterColumn);

            String fieldName = filterColumn.getFieldName();
            String fieldType = filterColumn.getFieldType();
            Integer operate = filterColumn.getOperate();
            String value = filterColumn.getValue();

            QueryFieldType queryFieldType = QueryFieldType.toEnumByValueIgnoreCase(fieldType);
            QueryOperator operator = QueryOperator.toEnumByValue(operate);
            switch (operator) {
                case GT:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.gt(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case GE:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.ge(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case LT:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.lt(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case LE:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.le(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case EQ:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.eq(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case NE:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.ne(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case LIKE:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.like(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case NOT_LIKE:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.notLike(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case BETWEEN:
                    assertValueNotBlank(filterColumn);
                    assertValueBetweenOperate(filterColumn);
                    String[] beValues = value.split(BETWEEN_SEPARATOR);
                    Object startValue = convertQueryValueClass(fieldName, queryFieldType, beValues[0]);
                    Object endValue = convertQueryValueClass(fieldName, queryFieldType, beValues[1]);
                    queryWrapper.between(fieldName, startValue, endValue);
                    break;
                case IN:
                    assertValueNotBlank(filterColumn);
                    assertValueInOperate(filterColumn);
                    String[] inValues = value.split(IN_SEPARATOR);
                    queryWrapper.in(fieldName, convertQueryValueClassList(fieldName, queryFieldType, inValues));
                    break;
                case LIKE_LEFT:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.likeLeft(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case LIKE_RIGHT:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.likeRight(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case IS_NULL:
                    queryWrapper.isNull(fieldName);
                    break;
                case IS_NOT_NULL:
                    queryWrapper.isNotNull(fieldName);
                    break;
                case NOT_LIKE_LEFT:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.notLikeLeft(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case NOT_LIKE_RIGHT:
                    assertValueNotBlank(filterColumn);
                    queryWrapper.notLikeRight(fieldName, convertQueryValueClass(fieldName, queryFieldType, value));
                    break;
                case null:
                    break;
            }

        }
    }

    private <M> void resolveSortList(QueryWrapper<M> queryWrapper) {
        assertSortListNotNull();

        for (SortColumn column : sortList) {
            validateSortColumn(column);

            String fieldName = column.getFieldName();
            QuerySortType sortType = QuerySortType.toEnumByValueIgnoreCase(column.getSortType());
            switch (sortType) {
                case DESC -> queryWrapper.orderByDesc(fieldName);
                case ASC -> queryWrapper.orderByAsc(fieldName);
                case null -> {
                }
            }
        }
    }

    private static void validateFilterColumn(FilterColumn column) {
        if (StringUtils.isBlank(column.getFieldName())) {
            throw new SpException(SimpleExceptionCode.QUERY_MODEL_TO_WRAPPER_HAS_BLANK_FIELD_NAME);
        }
        if (Objects.isNull(QueryFieldType.toEnumByValueIgnoreCase(column.getFieldType()))) {
            throw new SpException(SimpleExceptionCode.QUERY_MODEL_TO_WRAPPER_FIELD_TYPE_ERROR, column.getFieldName());
        }
        if (Objects.isNull(QueryOperator.toEnumByValue(column.getOperate()))) {
            throw new SpException(SimpleExceptionCode.QUERY_MODEL_TO_WRAPPER_OPERATOR_ERROR, column.getFieldName());
        }
    }

    private static void validateSortColumn(SortColumn column) {
        if (StringUtils.isBlank(column.getFieldName())) {
            throw new SpException(SimpleExceptionCode.QUERY_MODEL_TO_WRAPPER_HAS_BLANK_FIELD_NAME);
        }
        if (Objects.isNull(QuerySortType.toEnumByValueIgnoreCase(column.getSortType()))) {
            throw new SpException(SimpleExceptionCode.QUERY_MODEL_TO_WRAPPER_SORT_TYPE_ERROR, column.getFieldName());
        }
    }

    private static void assertPageInfoHasValue(PageInfo pageInfo) {
        if (Objects.isNull(pageInfo.getCurrentPage())) {
            pageInfo.setDefaultCurrentPage();
        }
        if (Objects.isNull(pageInfo.getPageSize())) {
            pageInfo.setDefaultPageSize();
        }
    }


    /**
     * 转换字段值的类型
     *
     * @param fieldName      字段名
     * @param queryFieldType 字段类型枚举
     * @param value          字符串值
     * @return 转换后的值
     */
    private static Object convertQueryValueClass(String fieldName, QueryFieldType queryFieldType, String value) {
        Function<String, Object> convertFunc = QueryFieldType.convertFuncMap.get(queryFieldType);
        try {
            return convertFunc.apply(value);
        } catch (Exception e) {
            LogUtil.error("QueryModel类型转换失败", e);
            throw new SpException(SimpleExceptionCode.QUERY_MODEL_TO_WRAPPER_FIELD_VALUE_CONVERT_FAILED, fieldName, value, queryFieldType.getValue());
        }
    }

    /**
     * 转换多个字段值的类型
     *
     * @param fieldName      字段名
     * @param queryFieldType 字段类型枚举
     * @param values         字符串值
     * @return 转换后的值
     */
    private static List<Object> convertQueryValueClassList(String fieldName, QueryFieldType queryFieldType, String[] values) {
        List<Object> valueList = new ArrayList<>();
        for (String value : values) {
            valueList.add(convertQueryValueClass(fieldName, queryFieldType, value));
        }
        return valueList;
    }

    private static void assertValueNotBlank(FilterColumn column) {
        if (StringUtils.isBlank(column.getValue())) {
            throw new SpException(SimpleExceptionCode.QUERY_MODEL_TO_WRAPPER_FIELD_VALUE_BLANK, column.getFieldName());
        }
    }

    private static void assertValueBetweenOperate(FilterColumn column) {
        if (!column.getValue().contains(BETWEEN_SEPARATOR)) {
            throw new SpException(SimpleExceptionCode.QUERY_MODEL_TO_WRAPPER_VALUE_NO_BETWEEN_SEPARATOR);
        }
    }

    private static void assertValueInOperate(FilterColumn column) {
        if (!column.getValue().contains(IN_SEPARATOR)) {
            throw new SpException(SimpleExceptionCode.QUERY_MODEL_TO_WRAPPER_VALUE_NO_IN_SEPARATOR);
        }
    }
}
