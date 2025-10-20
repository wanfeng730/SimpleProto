package cn.wanfeng.sp.util;

import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @date: 2024-11-20 14:55
 * @author: luozh.wanfeng
 * @description: 流工具
 * @since: 1.0
 */
public class InputStreamUtils {

    /**
     * 获取流的大小
     * @param inputStream 流
     * @return 大小
     */
    public static long getAvailable(InputStream inputStream){
        try {
            return inputStream.available();
        } catch (IOException e) {
            LogUtil.info("InputStream.available 失败", e);
        }
        return 0L;
    }

    /**
     * 优雅关闭流
     * @param inputStream 流
     */
    public static void closeQuietly(InputStream inputStream) {
        if (Objects.isNull(inputStream)) {
            return;
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            LogUtil.error("InputStream.close 失败", e);
        }
    }

    /**
     * 流转换为字符串 UTF-8
     * @param inputStream 流
     * @return 字符串内容
     */
    public static String getStringContent(InputStream inputStream){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n'); // 添加换行符以保持原样（如果有的话）
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 从文件中获取ByteArrayInputStream流
     *
     * @param file 文件
     * @return ByteArrayInputStream
     */
    public static ByteArrayInputStream getByteArrayInputStreamFromFile(File file) {
        ByteArrayInputStream byteArrayInputStream = null;
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            byte[] fileBytes = byteArrayOutputStream.toByteArray();
            LogUtil.debug("InputStreamUtils.getByteArrayInputStreamFromFile[fileBytes.length={}]", fileBytes.length);
            byteArrayInputStream = new ByteArrayInputStream(fileBytes);

            byteArrayOutputStream.close();
        } catch (Exception e) {
            LogUtil.error("从文件获取ByteArrayInputStream异常", e);
        }
        return byteArrayInputStream;
    }


    /**
     * 从字符串获取ByteArrayInputStream
     * @param content 字符串内容
     * @return ByteArrayInputStream
     */
    public static ByteArrayInputStream getByteArrayInputStreamFromString(String content) {
        if(StringUtils.isNotBlank(content)){
            return null;
        }
        try {
            return new ByteArrayInputStream(content.getBytes());
        } catch (Exception e) {
            LogUtil.error("从字符串获取ByteArrayInputStream异常", e);
        }
        return null;
    }

    /**
     * 从url链接获取ByteArrayInputStream
     * @param url 链接
     * @return ByteArrayInputStream
     */
    public static ByteArrayInputStream getByteArrayInputStreamFromHttp(String url){
        if(StringUtils.isBlank(url)){
            return null;
        }
        byte[] bytes = HttpUtil.downloadBytes(url);
        return new ByteArrayInputStream(bytes);
    }
}
