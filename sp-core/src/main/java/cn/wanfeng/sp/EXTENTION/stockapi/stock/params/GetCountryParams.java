package cn.wanfeng.sp.EXTENTION.stockapi.stock.params;

import cn.wanfeng.sp.EXTENTION.stockapi.common.CommonGetParams;
import lombok.Getter;

/**
 * GetCountryParams: 获取国家/地区信息参数.
 *
 * @date: 2025-02-10 16:51
 * @author: luozh.wanfeng
 */

@Getter
public class GetCountryParams extends CommonGetParams {

    /**
     * 国家/地区代码。采用ISO国家/地区代码（Country Code）
     */
    private String countryCode;

    public GetCountryParams() {

    }

    public GetCountryParams(String countryCode) {
        super();
        this.countryCode = countryCode;
    }
}
