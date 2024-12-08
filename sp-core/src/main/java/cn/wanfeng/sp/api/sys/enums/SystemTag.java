package cn.wanfeng.sp.api.sys.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @date: 2024-12-08 16:55
 * @author: luozh.wanfeng
 * @description: 系统标签，表名一个SysObject是什么类型的数据（如文件夹、文件、图片文件、视频文件）
 * @since: 1.0
 */
public enum SystemTag {

    FOLDER("sys_folder", "文件夹"),

    FILE("sys_file", "文件"),

    FILE_TEXT("sys_file_text", "文本文件"),

    FILE_IMAGE("sys_file_image", "图片文件"),

    FILE_VIDEO("sys_file_video", "视频文件"),

    NONE("The Tag is Not Support", "该文件类型不支持");


    private String value;

    private String valueCN;

    SystemTag(String value, String valueCN) {
        this.value = value;
        this.valueCN = valueCN;
    }

    public String getValue() {
        return value;
    }

    public String getValueCN() {
        return valueCN;
    }

    public static SystemTag toEnum(String value){
        for (SystemTag tag : values()) {
            if(StringUtils.equals(tag.getValue(), value)){
                return tag;
            }
        }
        return NONE;
    }
}
