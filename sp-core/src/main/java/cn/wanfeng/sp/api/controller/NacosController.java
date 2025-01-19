package cn.wanfeng.sp.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NacosController: desc.
 *
 * @date: 2025-01-19 16:44
 * @author: luozh.wanfeng
 */
@Tag(name = "Nacos配置模块")
@RestController
@RequestMapping("/nacos")
public class NacosController {

    @Value("${wanfeng.nacos.content:}")
    private String swaggerAuthor;

    @Operation(summary = "测试获取配置")
    @GetMapping("/get_config_test")
    public String getConfigTest(){
        return swaggerAuthor;
    }
}
