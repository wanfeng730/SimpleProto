package cn.wanfeng.sp.EXTENTION.stockapi.common;

import cn.wanfeng.sp.config.custom.SimpleStockConfig;
import lombok.Getter;

/**
 * CommonGetParams: 通用请求参数.
 *
 * @date: 2025-02-10 16:58
 * @author: luozh.wanfeng
 */
@Getter
public class CommonGetParams {

    private final String token;

    private final String format;

    private final String columns;

    public CommonGetParams() {
        this.token = SimpleStockConfig.apiToken;
        this.format = SimpleStockConfig.apiResultFormat;
        this.columns = null;
    }

    public CommonGetParams(String columns) {
        this.token = SimpleStockConfig.apiToken;
        this.format = SimpleStockConfig.apiResultFormat;
        this.columns = columns;
    }
}
