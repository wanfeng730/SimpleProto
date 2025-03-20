package cn.wanfeng.sp.EXTENTION.stockapi.stock;

import cn.hutool.http.HttpUtil;
import cn.wanfeng.sp.EXTENTION.stockapi.common.CommonGetResult;
import cn.wanfeng.sp.EXTENTION.stockapi.constant.TsanghiApiParamNames;
import cn.wanfeng.sp.EXTENTION.stockapi.constant.TsanghiApiUrls;
import cn.wanfeng.sp.EXTENTION.stockapi.stock.dto.CountryDTO;
import cn.wanfeng.sp.EXTENTION.stockapi.stock.params.GetCountryParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * TsanghiStockBaseInfoRepository: 沧海数据 获取股票基本信息.
 *
 * @date: 2025-02-10 17:13
 * @author: luozh.wanfeng
 */
@Repository
public class TsanghiStockBaseInfoRepository implements StockBaseInfoRepository {

    /**
     * 获取国家地区信息
     *
     * @param params 参数
     * @return 返回结果
     */
    @Override
    public CommonGetResult<CountryDTO> getCountryList(GetCountryParams params) {
        String countryCode = params.getCountryCode();

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(TsanghiApiParamNames.TOKEN, params.getToken());
        requestParams.put(TsanghiApiParamNames.FMT, params.getFormat());
        if(StringUtils.isNotBlank(countryCode)){
            requestParams.put(TsanghiApiParamNames.COUNTRY_CODE, countryCode);
        }

        String response = HttpUtil.get(TsanghiApiUrls.GET_COUNTRY, requestParams);

        return CommonGetResult.build(response, CountryDTO.class);
    }
}
