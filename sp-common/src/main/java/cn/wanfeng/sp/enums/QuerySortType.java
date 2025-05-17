package cn.wanfeng.sp.enums;

import lombok.Getter;

/**
 * QuerySortType: 查询排序方式.
 *
 * @date: 2025-05-17 15:52
 * @author: luozh.wanfeng
 */
@Getter
public enum QuerySortType {

    ASC("asc", "正序"),

    DESC("desc", "倒序");


    private final String value;

    private final String desc;

    QuerySortType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static QuerySortType toEnumByValueIgnoreCase(String value) {
        for (QuerySortType querySortType : values()) {
            if (querySortType.value.equalsIgnoreCase(value)) {
                return querySortType;
            }
        }
        return null;
    }
}
