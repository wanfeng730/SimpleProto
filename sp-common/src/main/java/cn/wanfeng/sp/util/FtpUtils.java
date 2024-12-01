package cn.wanfeng.sp.util;


import cn.hutool.core.io.FileUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @date: 2024-11-09 00:42
 * @author: luozh.wanfeng
 * @description: FTP工具类
 * @since: 1.0
 */
public class FtpUtils {

    /**
     * 获取一个ftp客户端
     * @param host ip地址
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     * @return FTPClient
     */
    public static FTPClient getClient(String host, Integer port, String username, String password){
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接服务器
            ftpClient.connect(host, port);

            int reply = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)){
                LogUtils.error("无法连接至FTP服务器[]", host, port);
                ftpClient.disconnect();
                return null;
            }

            // 登入服务器
            boolean login = ftpClient.login(username, password);
            if(!login){
                LogUtils.error("FTP登录失败，用户名或密码错误, username={}, password={}", username, password);
                ftpClient.logout();
                ftpClient.disconnect();
                return null;
            }

            // 设置通道字符集， 要与服务端设置一致
            ftpClient.setControlEncoding("UTF-8");
            // 设置文件传输编码类型， 字节传输：BINARY_FILE_TYPE, 文本传输：ASCII_FILE_TYPE， 建议使用BINARY_FILE_TYPE进行文件传输
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 动模式: enterLocalActiveMode(),被动模式: enterLocalPassiveMode(),一般选择被动模式
            ftpClient.enterLocalPassiveMode();
        } catch (Exception e) {
            LogUtils.error("获取ftp连接失败", e);
            throw new RuntimeException(e);
        }
        LogUtils.debug("FTP服务器[ftp://{}:{}]登录成功", host, port);
        return ftpClient;
    }

    /**
     * 断开ftp连接
     * @param ftpClient ftp连接客户端
     */
    public static void closeClient(FTPClient ftpClient){
        if(ftpClient == null){
            return;
        }
        try {
            LogUtils.debug("断开ftp连接， host:{}, port:{}", ftpClient.getPassiveHost(), ftpClient.getPassivePort());
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (Exception e){
            LogUtils.error("ftp连接断开异常，请检查");
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取目录下的文件列表
     *
     * @param ftpClient        ftp连接客户端
     * @param remoteFolderPath 远程目录路径
     * @return ftp文件列表
     */
    public static List<FTPFile> listFTPFileByRemoteFolderPath(@NotNull FTPClient ftpClient, @NotBlank String remoteFolderPath) {
        remoteFolderPath = adaptChineseEncoding(remoteFolderPath);
        FTPFile[] ftpFiles;
        try {
            ftpFiles = ftpClient.listFiles(remoteFolderPath);
        } catch (Exception e) {
            LogUtils.error("获取ftp目录下的文件列表失败", e);
            throw new RuntimeException(e);
        }
        return new ArrayList<>(Arrays.asList(ftpFiles));
    }

    /**
     * 文件下载：下载后的文件名与源文件同名
     * @param ftpClient ftp连接客户端
     * @param remoteFilePath 源文件路径（ftp服务端共享的目录的下一级开始）
     * @param downloadFolderPath 文件下载所在目录路径
     * @return 下载的文件
     */
    public static File downloadWithTargetFolder(@NotNull FTPClient ftpClient, @NotBlank String remoteFilePath, @NotBlank String downloadFolderPath){
        String targetFilePath = downloadFolderPath + File.separator + FileUtil.getName(remoteFilePath);
        return downloadWithTargetFile(ftpClient, remoteFilePath, targetFilePath);
    }

    /**
     * 文件下载：可以自定义下载后的文件名，可以用来修改文件后缀格式
     * @param ftpClient ftp连接客户端
     * @param remoteFilePath 源文件路径（ftp服务端共享的目录的下一级开始）
     * @param targetFilePath 文件下载的路径（完整路径）
     * @return 下载的文件
     */
    public static File downloadWithTargetFile(@NotNull FTPClient ftpClient, @NotBlank String remoteFilePath, @NotBlank String targetFilePath){
        // 中文目录处理存在问题， 转化为ftp能够识别中文的字符集
        String remotePath = adaptChineseEncoding(remoteFilePath);

        File downloadFile;
        try {
            //从ftp获取流
            InputStream inputStream = ftpClient.retrieveFileStream(remotePath);
            if (inputStream == null) {
                LogUtils.error("{}在ftp服务器中不存在，请检查", remoteFilePath);
                return null;
            }
            //新建本地下载文件
            downloadFile = new File(targetFilePath);
            if(downloadFile.exists()){
                LogUtils.info("文件{}已存在，将进行覆盖...", targetFilePath);
                FileUtil.del(downloadFile);
            }
            FileUtil.newFile(targetFilePath);
            //将流写入本地文件
            FileUtil.writeFromStream(inputStream, downloadFile);
            //关闭流
            inputStream.close();

            // 关闭流之后必须执行，否则下一个文件导致流为空
            boolean complete = ftpClient.completePendingCommand();
            if(complete){
                LogUtils.info("文件{}下载完成", remotePath);
            }else{
                LogUtils.error("文件{}下载失败", remotePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return downloadFile;
    }


    private static String adaptChineseEncoding(String content) {
        try {
            return new String(content.getBytes(StandardCharsets.UTF_8), FTP.DEFAULT_CONTROL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return content;
        }
    }

}
