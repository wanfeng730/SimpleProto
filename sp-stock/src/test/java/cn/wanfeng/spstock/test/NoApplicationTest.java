package cn.wanfeng.spstock.test;

import cn.wanfeng.sp.util.LogUtil;
import cn.wanfeng.spstock.api.stock.info.dto.CountryDTO;
import cn.wanfeng.spstock.api.common.CommonGetResult;
import org.junit.jupiter.api.Test;

/**
 * NoApplicationTest: desc.
 *
 * @date: 2025-02-10 17:42
 * @author: luozh.wanfeng
 */
public class NoApplicationTest {

    @Test
    public void test(){
        String json = "{\"msg\":\"操作成功\",\"code\":200,\"data\":[{\"country_code\":\"DNK\",\"country_name\":\"丹麦\"},{\"country_code\":\"PHL\",\"country_name\":\"菲律宾\"},{\"country_code\":\"CHE\",\"country_name\":\"瑞士\"},{\"country_code\":\"HUN\",\"country_name\":\"匈牙利\"},{\"country_code\":\"TUR\",\"country_name\":\"土耳其\"},{\"country_code\":\"AUS\",\"country_name\":\"澳大利亚\"},{\"country_code\":\"CAN\",\"country_name\":\"加拿大\"},{\"country_code\":\"CHL\",\"country_name\":\"智利\"},{\"country_code\":\"NLD\",\"country_name\":\"荷兰\"},{\"country_code\":\"ARG\",\"country_name\":\"阿根廷\"}]}";
        CommonGetResult<CountryDTO> result = CommonGetResult.build(json, CountryDTO.class);
        LogUtil.info("sdf");
    }
}
