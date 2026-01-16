package cn.wanfeng.sp.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * HttpServletResponseUtil: desc.
 *
 * @date: 2026-01-14 17:56
 * @author: wanfeng·Aura
 */
public class HttpServletResponseUtil {

    /**
     * 将文件流写入响应体
     * @param request 请求体
     * @param response 响应体
     * @param file 本地文件
     */
    public static void stream(HttpServletRequest request, HttpServletResponse response, File file){
        stream(request, response, file.getName(), InputStreamUtils.getByteArrayInputStreamFromFile(file));
    }

    /**
     * 将文件流写入响应体
     * @param request 请求体
     * @param response 响应体
     * @param filename 文件名
     * @param inputStream 文件流
     */
    public static void stream(HttpServletRequest request, HttpServletResponse response, String filename, InputStream inputStream){
        try {
            //设置响应头类型、文件名
            String encodeFilename = encodeFilename(request, filename);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", String.format("attachment; filename=%s", encodeFilename));

            ServletOutputStream out = response.getOutputStream();
            byte[] buffer = new byte[4096]; // 缓冲区大小
            int bytesRead; // 读取的字节数
            while ((bytesRead = inputStream.read(buffer)) != -1) { // 读取并写入输出流
                out.write(buffer, 0, bytesRead);
            }
            out.flush(); // 刷新输出流，确保所有数据都被发送
        } catch (Exception e) {
            LogUtil.error("文件写入response失败 file: {}", filename, e);
            throw new RuntimeException(e);
        } finally {
            //关闭流
            InputStreamUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 根据浏览器类型，对中文文件名进行编码兼容处理
     */
    private static String encodeFilename(HttpServletRequest request, String fileName) {
        try {
            String userAgent = request.getHeader("User-Agent");
            String encodeFileName;
            // IE浏览器 / Edge浏览器 做特殊编码
            if (Objects.nonNull(userAgent) && (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge"))) {
                encodeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
            } else {
                // 谷歌/火狐/其他浏览器：使用URL编码（标准）
                encodeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            }
            return encodeFileName;
        } catch (Exception e) {
            LogUtil.error("文件名编码失败 fileName: {}", fileName, e);
            throw new RuntimeException(e);
        }
    }
}
