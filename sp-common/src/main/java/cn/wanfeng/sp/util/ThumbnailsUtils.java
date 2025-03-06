package cn.wanfeng.sp.util;

import com.spire.doc.Document;
import com.spire.doc.documents.ImageType;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.graphics.PdfImageType;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * ThumbnailsUtils: 缩略图工具.
 *
 * @date: 2025-02-27 10:10
 * @author: luozh.wanfeng
 */
public class ThumbnailsUtils {

    /**
     * 图片文件生成缩略图
     *
     * @param imageFile 源文件
     * @return 缩略图文件
     */
    public static File generateThumbnailsFromImage(File imageFile) {
        // 生成缩略图取图片原格式
        String suffix = FileUtils.getSuffix(imageFile.getName());
        File out = new File(FileUtils.createTempDirectory(), imageFile.getName());
        try {
            Thumbnails.of(imageFile)
                    .outputFormat(suffix)
                    .size(160, 160)
                    .toFile(out);
        } catch (UnsupportedFormatException ufe) {
            LogUtil.error("生成缩略图异常，[{}]格式不支持", imageFile.getName());
            return null;
        } catch (Exception e) {
            LogUtil.error("生成缩略图异常" + e.getMessage(), e);
            return null;
        }
        return out;
    }

    /**
     * pdf第一页生成缩略图
     *
     * @param pdfFile pdf文件
     * @return 缩略图文件
     */
    public static File generateThumbnailsFromPdf(File pdfFile) {
        File outFolder = FileUtils.createTempDirectory();

        String imagePath;
        try {
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromFile(pdfFile.getPath());
            String fileName = pdfFile.getName();
            BufferedImage image = pdf.saveAsImage(0, PdfImageType.Bitmap);
            imagePath = outFolder.getPath() + File.separator + fileName + (1) + ".png";
            File imageFile = new File(imagePath);
            ImageIO.write(image, "PNG", imageFile);
        } catch (Exception e) {
            LogUtil.error("生成缩略图异常" + e.getMessage(), e);
            return null;
        }
        return new File(imagePath);
    }

    /**
     * doc文件生成缩略图
     *
     * @param docFile doc文件
     * @return 缩略图
     */
    public static File generateThumbnailsFromDoc(File docFile) {
        File outFolder = FileUtils.createTempDirectory();

        String imagePath;
        try {
            Document word = new Document();
            word.loadFromFile(docFile.getPath());
            String fileName = docFile.getName();
            BufferedImage image = word.saveToImages(0, ImageType.Bitmap);
            imagePath = outFolder.getPath() + File.separator + fileName + ".png";
            File file = new File(imagePath);
            ImageIO.write(image, "PNG", file);
        } catch (Exception e) {
            LogUtil.error("生成缩略图异常" + e.getMessage(), e);
            return null;
        }
        return new File(imagePath);
    }
}
