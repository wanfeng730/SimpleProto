package cn.wanfeng.sp.enums;

import lombok.Getter;

/**
 * QueryOperate: 查询操作符枚举.
 *
 * @date: 2025-05-17 15:33
 * @author: luozh.wanfeng
 */
@Getter
public enum QueryOperator {

    GT(1, "大于"),

    GE(2, "大于等于"),

    LT(3, "小于"),

    LE(4, "小于等于"),

    EQ(5, "等于"),

    NE(6, "不等于"),

    LIKE(7, "包含"),

    NOT_LIKE(8, "不包含"),

    BETWEEN(9, "范围（用~分割）"),

    IN(10, "可选项（多个值用,分隔）"),

    LIKE_LEFT(11, "左模糊查询（以...结尾）"),

    LIKE_RIGHT(12, "右模糊查询（以...开头）"),

    IS_NULL(13, "为空"),

    IS_NOT_NULL(14, "不为空"),

    NOT_LIKE_LEFT(15, "结尾不是"),

    NOT_LIKE_RIGHT(16, "开头不是");


    private final Integer value;

    private final String desc;

    QueryOperator(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static QueryOperator toEnumByValue(Integer value) {
        for (QueryOperator operator : values()) {
            if (operator.value.equals(value)) {
                return operator;
            }
        }
        return null;
    }
}
