package cn.wanfeng.sp.util;


import org.springframework.core.io.DefaultResourceLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @date: 2024-11-26 23:21
 * @author: luozh.wanfeng
 * @description: 资源文件工具类
 * @since: 1.0
 */
public class ResourceFileUtils {

    /**
     * 获取资源文件夹下的文件
     *
     * @param resourceFolderPath 资源文件夹路径
     * @return 文件列表
     */
    public static List<File> listResourceFolder(String resourceFolderPath) {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        try {
            File folder = defaultResourceLoader.getResource(resourceFolderPath).getFile();
            if (folder.isFile()) {
                return new ArrayList<>();
            }
            return Arrays.asList(Objects.requireNonNull(folder.listFiles()));
        } catch (Exception e) {
            LogUtil.error("获取资源文件夹[{}]的文件列表失败", resourceFolderPath, e);
        }
        return new ArrayList<>();
    }

    /**
     * 打印resources中的txt文件内容
     *
     * @param resourceFilePath 资源文件路径
     */
    public static String readFileContent(String resourceFilePath) {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        StringBuilder content = new StringBuilder();
        try {
            InputStream inputStream = defaultResourceLoader.getResource(resourceFilePath).getInputStream();
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
}
