package cn.wanfeng.sp.test;

import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.context.SimpleApplicationContext;
import cn.wanfeng.sp.storage.file.FileStorageClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @date: 2024-12-27 16:10
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public class SimpleApplicationContextTest extends SimpleprotoApplicationTest {

    @Test
    public void test(){
        FileStorageClient storageClient = SimpleApplicationContext.getBean(FileStorageClient.class);
        Assertions.assertNotNull(storageClient);
    }
}
