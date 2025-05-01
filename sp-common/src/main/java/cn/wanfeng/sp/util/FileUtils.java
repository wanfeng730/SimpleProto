package cn.wanfeng.sp.util;

import cn.hutool.core.io.FileUtil;
import cn.wanfeng.sp.exception.SpException;
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

    public static String getFileNameNoSuffix(String path){
        String name = getName(path);
        return name.substring(0, name.lastIndexOf("."));
    }

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
            throw new SpException(e, "创建临时文件夹异常");
        }
    }

    /**
     * 创建临时文件夹
     * @return 临时文件夹
     */
    public static File createTempDirectory(){
        return createTempDirectory("");
    }

    /**
     * 创建临时文件
     * @param fileName 文件名
     * @return 空文件
     */
    public static File createTempFile(String fileName){
        try {
            File tempDir = createTempDirectory();
            String tempFilePath = tempDir.getPath() + File.separator + fileName;
            File tempFile = new File(tempFilePath);
            boolean create = tempFile.createNewFile();
            return tempFile;
        } catch (Exception e) {
            LogUtil.error("创建临时文件{}异常", fileName, e);
        }
        return null;
    }
}
