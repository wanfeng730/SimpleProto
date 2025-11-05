package cn.wanfeng.sp.api.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @date: 2024-12-22 16:38
 * @author: luozh.wanfeng
 * @description: 文件类型
 * @since: 1.0
 */
public enum FileTag {

    IMAGE("image", "图片文件", new String[]{"jpg", "jpeg", "png", "bmp"}),
    TEXT("text", "文本文件", new String[]{"txt"}),
    AUDIO("audio", "音频文件", new String[]{"mp3"}),
    VIDEO("video", "视频文件", new String[]{"mp4"}),

    PDF("pdf", "PDF文件", new String[]{"pdf"}),
    OFD("ofd", "OFD文件", new String[]{"ofd"}),

    WORD("word", "doc文件", new String[]{"doc", "docx"}),
    EXCEL("excel", "excel文件", new String[]{"xls", "xlsx"}),
    PPT("ppt", "PPT文件", new String[]{"ppt", "pptx"}),

    ZIP("zip", "ZIP压缩包", new String[]{"zip"}),
    _7Z("7z", "7Z压缩包", new String[]{"7z"}),

    JAR("jar", "JAVA执行文件", new String[]{"jar"}),



    NONE("Not Supported", "不支持的文件类型", new String[]{});

    private String value;

    private String valueCN;

    private String[] suffixes;

    FileTag(String value, String valueCN, String[] suffixes) {
        this.value = value;
        this.valueCN = valueCN;
        this.suffixes = suffixes;
    }

    public String getValue() {
        return value;
    }

    public String getValueCN() {
        return valueCN;
    }

    /**
     * 根据tag值匹配
     * @param value tag值
     * @return FileTag
     */
    public static FileTag toEnum(String value){
        for (FileTag tag : values()) {
            if(StringUtils.equals(tag.getValue(), value)){
                return tag;
            }
        }
        return NONE;
    }

    /**
     * 根据文件后缀匹配
     * @param suffix 后缀
     * @return FileTag
     */
    public static FileTag toEnumBySuffix(String suffix){
        for (FileTag fileTag : values()) {
            if(Arrays.asList(fileTag.suffixes).contains(suffix)){
                return fileTag;
            }
        }
        return NONE;
    }
}
