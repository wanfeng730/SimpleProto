package cn.wanfeng.sp.test;


import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.api.domain.SpFile;
import cn.wanfeng.sp.api.enums.FileTag;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.sys.TestFolder;
import cn.wanfeng.sp.util.DateUtils;
import cn.wanfeng.sp.util.InputStreamUtils;
import cn.wanfeng.sp.util.LogUtil;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Date;

/**
 * @date: 2024-12-22 18:00
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public class SpFileTest extends SimpleprotoApplicationTest {

    @Resource
    private SpSession session;

    @Test
    public void test(){
        TestFolder testFolder = new TestFolder(session, UlidCreator.getUlid().toString(), "测试父文件夹", "TF", new Date());
        testFolder.store();

        File file = new File("D:\\WanfengHome\\fileSpace\\就业\\公租房补贴申请\\家庭经济状况告知书.pdf");
        SpFile spFile = new SpFile(session, "test_file", file.getName() + DateUtils.currentDateTime(), InputStreamUtils.getByteArrayInputStreamFromFile(file));
        spFile.setFileTag(FileTag.TEXT);
        spFile.move(testFolder);
        spFile.store();

        testFolder.remove();


        // // SpFile spFile1 = new SpFile(session, 139L);
        // // spFile1.setStorage(new File("D:\\WanfengHome\\fileSpace\\ImageRecord\\b1-2.png"));
        // // spFile1.store();
        // SpFile spFile = new SpFile(session, 139L);
        // spFile.remove();
        LogUtil.info("");
    }
}
