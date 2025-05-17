package cn.wanfeng.sp.web.controller;


import cn.wanfeng.sp.api.dataobject.SpUserDTO;
import cn.wanfeng.sp.model.FilterColumn;
import cn.wanfeng.sp.model.QueryModel;
import cn.wanfeng.sp.model.QueryResult;
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
    public QueryResult<SpUserDTO> listUser(@RequestBody QueryModel queryModel) {
        return spUserService.listUser(queryModel);
    }

    @Operation(summary = "简单获取用户列表")
    @PostMapping("simple_list_user")
    public QueryResult<SpUserDTO> simpleListUser(@RequestBody FilterColumn filterColumn) {
        QueryModel queryModel = new QueryModel();
        queryModel.addFilterColumn(filterColumn);
        return spUserService.listUser(queryModel);
    }

}
