package cn.wanfeng.sp.api.controller;


import cn.wanfeng.sp.api.domain.SpUser;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SimpleExceptionCode;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.session.SpBulkOperator;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.LogUtil;
import cn.wanfeng.sp.util.NumberUtils;
import cn.wanfeng.sp.util.PrettyLogUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * TestController: desc.
 *
 * @date: 2025-05-01 23:52
 * @author: luozh.wanfeng
 */
@Tag(name = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private SpSession session;

    @Resource
    private SpBulkOperator bulkOperator;

    @Operation(summary = "测试i18n国际化异常信息处理")
    @GetMapping("/testI18n")
    public void testI18n(){
        throw new SpException(SimpleExceptionCode.TEST);
    }

    @Operation(summary = "测试i18n国际化异常信息处理（带参数）")
    @GetMapping("/testI18nHasArgs")
    public void testI18nHasArgs(){
        throw new SpException(SimpleExceptionCode.TEST_HAS_ARGS, "愿此行，终抵群星！");
    }

    @Operation(summary = "测试批量操作")
    @GetMapping("testBulkStore")
    public void testBulkStore(){
        String USER_NUMBER_INCREASE_NAME = "userNumberIncr";

        Long userNumber = session.increaseLongByName(SimpleProtoConfig.appName + ":" + USER_NUMBER_INCREASE_NAME);
        String numberStr = NumberUtils.generateNumberFixLength(userNumber, 4);
        SpUser spUser1 = new SpUser(session, "user" + numberStr);
        SpUser spUser2 = new SpUser(session, "user" + numberStr + "-2");

        SpUser spUser3 = new SpUser(session, "user" + numberStr + "-3");
        spUser3.setName("第一次批量更新*****");

        List<SpUser> objectList = new ArrayList<>();
        objectList.add(spUser1);
        objectList.add(spUser2);
        objectList.add(spUser3);
        bulkOperator.bulkStore(objectList);

        for (SpUser spUser : objectList) {
            LogUtil.info("user:\n{}", PrettyLogUtil.prettyJson(spUser.getDocument()));
        }

        spUser1.setPassword("批量更新测试2*****");
        spUser2.setPassword("批量更新测试2*****");
        spUser3.setPassword("批量更新测试2*****");
        bulkOperator.bulkStore(objectList);

        bulkOperator.bulkRemove(objectList);
    }
}
