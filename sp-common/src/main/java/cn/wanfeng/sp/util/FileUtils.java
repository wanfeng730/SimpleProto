package cn.wanfeng.sp.util;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @date: 2024-12-21 16:40
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public class FileUtils extends FileUtil {

    /**
     * 创建临时文件夹
     * @param prefix 文件夹名称前缀
     * @return 临时文件夹
     */
    public static File createTempDirectory(String prefix){
        if(StringUtils.isBlank(prefix)){
            prefix = "";
        }
        try {
            Path path = Files.createTempDirectory(prefix);
            return path.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
