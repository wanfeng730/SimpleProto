package cn.wanfeng.sp.elastic;

/**
 * @date: 2024-11-07 21:43
 * @author: luozh.wanfeng
 * @description: es 日期格式
 * @since: 1.0
 */
public enum ElasticDateTimePattern {
    /**
     * 毫秒
     */
    EPOCH_SECOND("epoch_second"),

    DATE_TIME_MILLIS("yyyy-MM-dd HH:mm:ss.SSS"),

    DATE_TIME("yyyy-MM-dd HH:mm:ss"),

    DATE("yyyy-MM-dd")

    ;


    private String pattern;

    ElasticDateTimePattern(String pattern) {
        this.pattern = pattern;
    }

    public String toPattern(){
        return this.pattern;
    }
}
