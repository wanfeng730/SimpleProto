package cn.wanfeng.spstock.api.common;

import cn.wanfeng.spstock.config.SimpleStockConfig;
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

    public CommonGetParams(String columns) {
        this.token = SimpleStockConfig.apiToken;
        this.format = SimpleStockConfig.apiResultFormat;
        this.columns = columns;
    }
}
