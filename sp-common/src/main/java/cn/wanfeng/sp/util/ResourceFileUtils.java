package cn.wanfeng.sp.util;


import org.springframework.core.io.DefaultResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @date: 2024-11-26 23:21
 * @author: luozh.wanfeng
 * @description: 资源文件工具类
 * @since: 1.0
 */
public class ResourceFileUtils {

    /**
     * 获取资源文件的文件流
     * @param resourceFilePath 资源文件路径
     * @return 文件流
     */
    public static InputStream getInputStream(String resourceFilePath){
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        try {
            return defaultResourceLoader.getResource(resourceFilePath).getInputStream();
        } catch (IOException e) {
            LogUtil.error("获取资源文件流失败 路径：{}", resourceFilePath);
            return null;
        }
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
