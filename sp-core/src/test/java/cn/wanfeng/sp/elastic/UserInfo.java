package cn.wanfeng.sp.elastic;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @date: 2024-10-31 23:00
 * @author: luozh.wanfeng
 */
@Data
@AllArgsConstructor
@Document(indexName = "user_info")
public class UserInfo {

    @Id
    private Long id;

    /**
     * 工号
     */
    @Field(name = "job_no")
    private String jobNo;

    /**
     * 姓名
     */
    @Field(name = "name")
    private String name;

    /**
     * 英文名
     */
    @Field(name = "english_name")
    private String englishName;

    /**
     * 工作岗位
     */
    private String job;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 薪资
     */
    private BigDecimal salary;

    /**
     * 入职时间
     */
    @Field(name = "job_day", format = DateFormat.date_hour_minute_second_fraction)
    private Date jobDay;

    /**
     * 备注
     */
    private String remark;

    public static List<UserInfo> generateTestData() {
        List<UserInfo> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            list.add(new UserInfo(1001L, "2001", "张三", "zhangsan", "Java", 1, 19, new BigDecimal("12500.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
            list.add(new UserInfo(1002L, "2002", "李四", "lisi", "PHP", 1, 18, new BigDecimal("11600.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
            list.add(new UserInfo(1003L, "2003", "王五", "wangwu", "C++", 1, 20, new BigDecimal("9900.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
            list.add(new UserInfo(1004L, "2004", "赵六", "zhaoliu", "Java Leader", 1, 20, new BigDecimal("20000.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
            list.add(new UserInfo(1005L, "2005", "小五", "xiaowu", "H5", 1, 17, new BigDecimal("10600.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
            list.add(new UserInfo(1006L, "2006", "小六", "xaioliu", "web", 1, 20, new BigDecimal("12600.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
            list.add(new UserInfo(1007L, "2007", "小七", "xiaoqi", "app", 1, 22, new BigDecimal("20000.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
            list.add(new UserInfo(1008L, "2008", "小八", "xaioba", "Java", 1, 21, new BigDecimal("11000.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
            list.add(new UserInfo(1009L, "2009", "小九", "xiaojiu", "Java", 1, 20, new BigDecimal("14000.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
            list.add(new UserInfo(1010L, "2010", "大十", "dashi", "Java", 1, 20, new BigDecimal("13000.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}