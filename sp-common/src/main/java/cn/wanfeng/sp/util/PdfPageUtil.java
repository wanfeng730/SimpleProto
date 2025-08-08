package cn.wanfeng.sp.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

/**
 * PdfPageUtil: pdf文件页工具类.
 *
 * @date: 2025-08-08 10:45
 * @author: luozh.wanfeng
 */
public class PdfPageUtil {

    /**
     * 获取pdf总页数
     *
     * @param pdfFilePath pdf文件路径
     * @return 总页数
     */
    public static Integer getTotalPage(String pdfFilePath) {
        try {
            PdfReader reader = new PdfReader(pdfFilePath);
            PdfDocument pdfDoc = new PdfDocument(reader);
            Integer totalPage = pdfDoc.getNumberOfPages();
            pdfDoc.close();
            return totalPage;
        } catch (Exception e) {
            LogUtil.error("获取pdf页数失败: {}", pdfFilePath, e);
            return -1;
        }
    }
}
