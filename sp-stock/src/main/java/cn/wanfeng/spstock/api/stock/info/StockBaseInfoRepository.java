package cn.wanfeng.spstock.api.stock.info;

import cn.wanfeng.spstock.api.stock.info.dto.CountryDTO;
import cn.wanfeng.spstock.api.stock.info.params.GetCountryParams;
import cn.wanfeng.spstock.api.common.CommonGetResult;

/**
 * StockBaseInfoRepository: 股票 基本信息获取接口.
 *
 * @date: 2025-02-10 16:37
 * @author: luozh.wanfeng
 */
public interface StockBaseInfoRepository {

    /**
     * 获取国家地区信息
     * @param params 参数
     * @return 返回结果
     */
    CommonGetResult<CountryDTO> getCountryList(GetCountryParams params);
}
