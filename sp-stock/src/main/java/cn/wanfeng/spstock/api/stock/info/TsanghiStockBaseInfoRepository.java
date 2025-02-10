package cn.wanfeng.spstock.api.stock.info;

import cn.hutool.http.HttpUtil;
import cn.wanfeng.spstock.api.common.CommonGetResult;
import cn.wanfeng.spstock.api.constant.TsanghiApiParamNames;
import cn.wanfeng.spstock.api.constant.TsanghiApiUrls;
import cn.wanfeng.spstock.api.stock.info.dto.CountryDTO;
import cn.wanfeng.spstock.api.stock.info.params.GetCountryParams;
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
        String token = params.getToken();
        String format = params.getFormat();
        String columns = params.getColumns();

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(TsanghiApiParamNames.TOKEN, params.getToken());


        String response = HttpUtil.get(TsanghiApiUrls.GET_COUNTRY, requestParams);

        return CommonGetResult.build(response, CountryDTO.class);
    }
}
