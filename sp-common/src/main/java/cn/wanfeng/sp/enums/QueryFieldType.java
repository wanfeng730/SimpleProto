package cn.wanfeng.sp.enums;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * QueryFieldType: 查询字段类型.
 *
 * @date: 2025-05-17 15:45
 * @author: luozh.wanfeng
 */
@Getter
public enum QueryFieldType {

    INT("int", "整型"),

    LONG("long", "长整型"),

    STRING("string", "字符串"),

    BOOL("bool", "布尔值"),

    DATE("date", "日期");


    private final String value;

    private final String desc;

    QueryFieldType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static QueryFieldType toEnumByValueIgnoreCase(String value) {
        for (QueryFieldType queryFieldType : values()) {
            if (queryFieldType.getValue().equalsIgnoreCase(value)) {
                return queryFieldType;
            }
        }
        return null;
    }

    public static final Map<QueryFieldType, Function<String, Object>> convertFuncMap = new HashMap<>();

    static {
        convertFuncMap.put(INT, value -> Integer.parseInt(value));
        convertFuncMap.put(LONG, value -> Long.parseLong(value));
        convertFuncMap.put(STRING, value -> value);
        convertFuncMap.put(BOOL, value -> Boolean.parseBoolean(value));
        convertFuncMap.put(DATE, value -> DateUtil.parse(value));
    }
}
