package cn.wanfeng.sp.api.controller;


import cn.wanfeng.sp.api.dataobject.SpUserDTO;
import cn.wanfeng.sp.api.service.SpUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date: 2025-01-11 16:21
 * @author: luozh.wanfeng
 * @description: 用户模块
 * @since: 1.1
 */
@Tag(name = "【SimpleProto】用户模块")
@RestController
@RequestMapping("/user")
public class SpUserController {

    @Resource
    private SpUserService spUserService;

    @Operation(summary = "用户详情")
    @GetMapping("/detail_user")
    public SpUserDTO detailUser(@RequestParam String userId){
        return spUserService.detail(Long.parseLong(userId));
    }
}
