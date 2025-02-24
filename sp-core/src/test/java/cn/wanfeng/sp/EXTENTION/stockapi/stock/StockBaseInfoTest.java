package cn.wanfeng.sp.EXTENTION.stockapi.stock;

import cn.wanfeng.sp.EXTENTION.stockapi.common.CommonGetResult;
import cn.wanfeng.sp.EXTENTION.stockapi.stock.info.StockBaseInfoRepository;
import cn.wanfeng.sp.EXTENTION.stockapi.stock.info.dto.CountryDTO;
import cn.wanfeng.sp.EXTENTION.stockapi.stock.info.params.GetCountryParams;
import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.util.LogUtil;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * StockBaseInfoTest: desc.
 *
 * @date: 2025-02-21 16:01
 * @author: luozh.wanfeng
 */
public class StockBaseInfoTest extends SimpleprotoApplicationTest {

    @Resource
    private StockBaseInfoRepository stockBaseInfoRepository;

    @Test
    public void test(){
        GetCountryParams getCountryParams = new GetCountryParams();
        CommonGetResult<CountryDTO> result = stockBaseInfoRepository.getCountryList(getCountryParams);
        LogUtil.info("获取国家/地区测试完成, result={}", JSON.toJSONString(result));
    }
}
