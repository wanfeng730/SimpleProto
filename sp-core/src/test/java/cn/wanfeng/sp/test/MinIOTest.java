package cn.wanfeng.sp.test;


import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.storage.file.FileStorageClient;
import cn.wanfeng.sp.util.DateUtils;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @date: 2024-12-19 23:25
 * @author: luozh.wanfeng
 */
public class MinIOTest extends SimpleprotoApplicationTest {

    @Resource
    private FileStorageClient fileStorageClient;

    @Test
    public void test() throws IOException {
        File file = new File("D:\\WanfengHome\\fileSpace\\就业\\公租房补贴申请\\家庭经济状况告知书.pdf");
        String storageKey = "test/" + DateUtils.currentDateTime() + "/" +file.getName();
        fileStorageClient.setObject(storageKey, file);

        InputStream inputStream = fileStorageClient.getObjectStream(storageKey);
        LogUtil.info("获取到{}的文件大小为{}", storageKey, inputStream.available());
    }

}
