package cn.wanfeng.sp.test;


import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.storage.file.FileStorageClient;
import cn.wanfeng.sp.util.DateUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
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
        File file1 = new File("/Users/wanfeng/Desktop/XY\uD83D\uDC95/以高水平环境保护推动乡村高...展——以湖州市安吉余村为例_延予.pdf");
        String storageKey = "test/" + DateUtils.currentDateTime() + "/" +file1.getName();
        fileStorageClient.setObject(storageKey, file1);

        InputStream inputStream = fileStorageClient.getObjectStream(storageKey);
        Assertions.assertTrue(inputStream.available() > 0);

        File downloadFile = fileStorageClient.downloadObject(storageKey);
        Assertions.assertTrue(downloadFile.exists());

        String previewUrl = fileStorageClient.getObjectPreviewUrl(storageKey, 120);
        Assertions.assertTrue(StringUtils.isNotBlank(previewUrl));

        fileStorageClient.removeObject(storageKey);
        Assertions.assertNull(fileStorageClient.getObjectStream(storageKey));
    }

}
