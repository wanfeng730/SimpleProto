package cn.wanfeng.sp.util;

import cn.hutool.core.io.FileUtil;
import cn.wanfeng.sp.exception.SpException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

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

    public static String readFileContent(File file) {
        StringBuilder content = new StringBuilder();
        try {
            InputStream inputStream = InputStreamUtils.getByteArrayInputStreamFromFile(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            LogUtil.error("打印启动显示图失败", e);
        }
        return content.toString();
    }

    /**
     * 文件名添加当前时间后缀
     * @param filename 文件名
     * @param dateTimeFormat 时间格式
     * @return 文件名
     */
    public static String filenameAppendTimeSuffix(String filename, String dateTimeFormat){
        if(StringUtils.isBlank(filename) || StringUtils.isBlank(dateTimeFormat)){
            return filename;
        }
        String time = DateUtils.format(new Date(), dateTimeFormat);
        if(!filename.contains(".")){
            return filename + "_" + time;
        }
        int index = filename.lastIndexOf(".");
        String name = filename.substring(0, index);
        String suffix = filename.substring(index);
        return name + "_" + time + suffix;
    }

    /**
     * 文件名添加当前时间后缀 yyyyMMdd_HHmmss
     * @param filename 文件名
     * @return 文件名
     */
    public static String filenameAppendTimeSuffix(String filename){
        return filenameAppendTimeSuffix(filename, "yyyyMMdd_HHmmss");
    }


}
