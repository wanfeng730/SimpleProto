package cn.wanfeng.sp.util;

import java.io.*;
import java.util.Objects;

/**
 * @date: 2024-11-20 14:55
 * @author: luozh.wanfeng
 * @description: 流工具
 * @since: 1.0
 */
public class InputStreamUtils {

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
            LogUtil.error("InputStream.close 失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * 从文件中获取ByteArrayInputStream流
     *
     * @param file 文件
     * @return ByteArrayInputStream
     */
    public static ByteArrayInputStream getByteArrayInputStreamFromFile(File file) {
        ByteArrayInputStream byteArrayInputStream;
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
            throw new RuntimeException(e);
        }
        return byteArrayInputStream;
    }

}
