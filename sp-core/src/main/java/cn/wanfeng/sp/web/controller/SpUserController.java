package cn.wanfeng.sp.web.controller;


import cn.wanfeng.sp.api.dataobject.SpUserDTO;
import cn.wanfeng.sp.model.FilterColumn;
import cn.wanfeng.sp.model.QueryParameter;
import cn.wanfeng.sp.model.ListResult;
import cn.wanfeng.sp.util.ThreadUtil;
import cn.wanfeng.sp.web.service.SpUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @date: 2025-01-11 16:21
 * @author: luozh.wanfeng
 * @description: 用户模块
 * @since: 1.1
 */
@Tag(name = "[SimpleProto]用户模块")
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

    @Operation(summary = "获取用户列表")
    @PostMapping("list_user")
    public ListResult<SpUserDTO> listUser(@RequestBody QueryParameter queryParameter) {
        return spUserService.listUser(queryParameter);
    }

    @Operation(summary = "简单获取用户列表")
    @PostMapping("simple_list_user")
    public ListResult<SpUserDTO> simpleListUser(@RequestBody FilterColumn filterColumn) {
        ThreadUtil.sleepQuietly(1000);
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.addFilterColumn(filterColumn);
        return spUserService.listUser(queryParameter);
    }

}
