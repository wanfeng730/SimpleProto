package cn.wanfeng.sp.sys;


import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.session.SpSession;
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
        TestFolder testFolder1 = new TestFolder(spSession, folderId);
        Assertions.assertEquals("更新后的文件夹名称", testFolder1.getDisplayName());

    }


}
