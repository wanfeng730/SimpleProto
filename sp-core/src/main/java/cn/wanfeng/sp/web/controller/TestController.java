package cn.wanfeng.sp.web.controller;


import cn.wanfeng.sp.api.domain.SpUser;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SimpleExceptionCode;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.session.SpBulkOperator;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.storage.file.FileStorageClient;
import cn.wanfeng.sp.storage.file.FileStorageDTO;
import cn.wanfeng.sp.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.File;
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

    @Resource
    private FileStorageClient fileStorageClient;

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
        LogUtil.info("testPrettyLogUtil1: {}", PrettyLogUtil.prettyJsonIgnoreSuperClass(spUser1));
        objectList.add(spUser2);
        LogUtil.info("testPrettyLogUtil2: {}", PrettyLogUtil.toJsonIgnoreSuperClass(spUser1));
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

    @Operation(summary = "测试获取minio永久预览链接")
    @PostMapping("/testGetObjectPreviewUrl")
    public String testGetObjectPreviewUrl(@RequestBody String storageKey) {
        return fileStorageClient.getObjectPreviewUrl(storageKey);
    }

    @Operation(summary = "测试获取minio文件列表")
    @PostMapping("/testMinioListObject")
    public void testMinioListObject(){
        List<FileStorageDTO> fileStorageDTOList = fileStorageClient.listObject("test_file");
        LogUtil.info("fileStorageDTOList:\n{}", PrettyLogUtil.prettyJson(fileStorageDTOList));
    }

    @Operation(summary = "测试ResourceFileUtil")
    @PostMapping("/testResourceFileUtil")
    public void testResourceFileUtil() {
        List<File> fileList = ResourceFileUtils.listChildFile("domain_mappings");
        LogUtil.info("fileList: {}", fileList.size());

        for (File file : fileList) {
            String content = FileUtils.readFileContent(file);
            LogUtil.info("{} length: {}", file.getName(), content.length());
        }
    }

}
