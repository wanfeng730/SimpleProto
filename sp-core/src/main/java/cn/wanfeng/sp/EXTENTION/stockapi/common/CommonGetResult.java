package cn.wanfeng.sp.EXTENTION.stockapi.common;

import cn.wanfeng.sp.config.custom.SimpleStockConfig;
import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * CommonGetResult: 通用请求结果.
 *
 * @date: 2025-02-10 17:06
 * @author: luozh.wanfeng
 */
@Getter
public class CommonGetResult<DataType> {

    private final String message;

    private final Integer code;

    private final List<DataType> dataList;

    private CommonGetResult(String message, Integer code, List<DataType> dataList) {
        this.message = message;
        this.code = code;
        this.dataList = dataList;
    }

    public static <DataType> CommonGetResult<DataType> build(String response, Class<DataType> dataType){
        DocumentContext documentContext = JsonPath.parse(response);

        String message = tryReadMessage(documentContext);
        Integer code = tryReadCode(documentContext);
        String dataJson = tryReadDataJson(documentContext);

        List<DataType> dataList = JSON.parseArray(dataJson, dataType, SimpleStockConfig.responseParserConfig);

        return new CommonGetResult<>(message, code, dataList);
    }

    private static String tryReadMessage(DocumentContext documentContext){
        String message = documentContext.read("$.msg");
        if(StringUtils.isBlank(message)){
            message = documentContext.read("$.message");
        }
        return message;
    }

    private static Integer tryReadCode(DocumentContext documentContext){
        return documentContext.read("$.code");
    }

    private static String tryReadDataJson(DocumentContext documentContext){
        JSONArray data = documentContext.read("$.data");
        return data.toJSONString();
    }
}
