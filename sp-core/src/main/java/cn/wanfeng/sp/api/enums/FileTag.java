package cn.wanfeng.sp.api.enums;

import org.apache.commons.lang3.StringUtils;

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
    DOC("doc", "doc文件", new String[]{"doc"}),
    DOCX("docx", "docx文件", new String[]{"docx"}),

    NONE("The fileTag is Not Supported", "不支持的文件类型", new String[]{});

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

    public static FileTag toEnum(String value){
        for (FileTag tag : values()) {
            if(StringUtils.equals(tag.getValue(), value)){
                return tag;
            }
        }
        return NONE;
    }

}
