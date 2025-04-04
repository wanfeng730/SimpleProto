package cn.wanfeng.sp.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * SimpleLogUtil: 增强的日志输出工具类.
 *
 * @date: 2025-03-18 00:34
 * @author: luozh.wanfeng
 */
public class PrettyLogUtil {

    private static final String BLANK = StringUtils.EMPTY;

    public static void printPrettyJson(Object data){
        if(Objects.isNull(data)){
            System.out.println("data is Null");
            return;
        }
        System.out.println(prettyJson(data));
    }

    public static void printPrettyJson(String jsonString){
        if(StringUtils.isBlank(jsonString)){
            System.out.println("jsonString is Null or Blank");
        }
        System.out.println(prettyJson(jsonString));
    }


    public static String prettyJson(Object data){
        if(Objects.isNull(data)){
            return BLANK;
        }
        return JSON.toJSONString(data, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }

    public static String prettyJson(String jsonString){
        if(StringUtils.isBlank(jsonString)){
            System.out.println("jsonString is Null or Blank");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        return prettyJson(jsonObject);
    }
}
