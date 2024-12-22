package cn.wanfeng.sp.api.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @date: 2024-12-22 16:38
 * @author: luozh.wanfeng
 * @description: 文件类型
 * @since: 1.0
 */
public enum FileTag {

    IMAGE("image", "图片文件"),
    TEXT("text", "文本文件"),
    VIDEO("video", "视频文件"),

    NONE("The fileTag is Not Supported", "不支持的文件类型");

    private String value;

    private String valueCN;

    FileTag(String value, String valueCN) {
        this.value = value;
        this.valueCN = valueCN;
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
