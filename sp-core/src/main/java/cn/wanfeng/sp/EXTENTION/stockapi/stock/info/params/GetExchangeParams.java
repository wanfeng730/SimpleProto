package cn.wanfeng.sp.EXTENTION.stockapi.stock.info.params;

import lombok.Builder;
import lombok.Data;

/**
 * GetExchangeParams: desc.
 *
 * @date: 2025-02-10 16:46
 * @author: luozh.wanfeng
 */
@Data
@Builder
public class GetExchangeParams {
    /**
     * 交易所代码。
     * 采用ISO市场标识码MIC（Market identifier codes)，例如：NGXS（上交所）、SHE（深交所）、XMAS（纳斯达克）。
     */
    private String exchangeCode;
    /**
     * 国家/地区代码
     */
    private String countryCode;
}
