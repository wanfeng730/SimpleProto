package cn.wanfeng.sp.api.controller;


import cn.wanfeng.sp.exception.SimpleExceptionCode;
import cn.wanfeng.sp.exception.SpException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
