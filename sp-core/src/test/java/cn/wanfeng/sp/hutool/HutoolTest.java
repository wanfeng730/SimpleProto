package cn.wanfeng.sp.hutool;


import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @date: 2024-11-09 00:50
 * @author: luozh.wanfeng
 */
public class HutoolTest {

    @Test
    public void testFileUtil(){
        String filename = FileUtil.getName("AAA/BBB/123.txt");
        Assertions.assertEquals("123.txt", filename);

        filename = FileUtil.getName("");
        Assertions.assertEquals("", filename);

        filename = FileUtil.getName("/");
        Assertions.assertEquals("", filename);

        filename = FileUtil.getName("AAA");
        Assertions.assertEquals("AAA", filename);
    }

}
