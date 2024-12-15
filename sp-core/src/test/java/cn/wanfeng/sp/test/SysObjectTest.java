package cn.wanfeng.sp.test;


import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.api.dataobject.SpSysObjectDO;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.sys.TestFolder;
import cn.wanfeng.sp.sys.TestFolderDO;
import cn.wanfeng.sp.sys.mapper.search.TestFolderMapper;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

/**
 * @date: 2024-12-09 22:19
 * @author: luozh.wanfeng
 * @description: SpSysObject测试
 * @since: 1.0
 */
public class SysObjectTest extends SimpleprotoApplicationTest {

    @Resource
    private SpSession spSession;

    @Resource
    private TestFolderMapper testFolderMapper;

    @Test
    public void test(){
        // test create
        TestFolder testFolder = new TestFolder(spSession, UlidCreator.getUlid().toString(), "测试文件夹名称", "TF", new Date());
        testFolder.store();


        List<TestFolderDO> testFolderDOList = testFolderMapper.findAll();
        Assertions.assertTrue(CollectionUtils.isNotEmpty(testFolderDOList));

        Date date = new Date();
        testFolder.setDisplayName("更新后的文件夹名称");
        testFolder.setExpireDate(date);
        testFolder.store();

        Long folderId = testFolder.getId();
        testFolder = new TestFolder(spSession, folderId);
        Assertions.assertEquals("更新后的文件夹名称", testFolder.getDisplayName());

        TestFolder testFolder2 = new TestFolder(spSession, UlidCreator.getUlid().toString(), "测试子文件夹", "TF", new Date());
        testFolder2.move(testFolder.getId());
        testFolder2.store();
        Assertions.assertEquals(testFolder.getPath(), testFolder2.getParentPath());

        TestFolder testFolder3 = new TestFolder(spSession, UlidCreator.getUlid().toString(), "测试子子文件夹", "TFFF", new Date());
        testFolder3.move(testFolder2.getPath());
        testFolder3.store();
        Assertions.assertEquals(testFolder2.getPath(), testFolder3.getParentPath());

        testFolder.remove();
        SpSysObjectDO spSysObjectDO = spSession.databaseStorage().findSysObjectByPath(SimpleProtoConfig.dataTable, testFolder.getPath());
        Assertions.assertNull(spSysObjectDO);
    }


}
