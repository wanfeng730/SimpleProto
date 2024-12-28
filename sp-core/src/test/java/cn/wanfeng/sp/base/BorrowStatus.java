package cn.wanfeng.sp.base;

import cn.wanfeng.sp.anno.ProtoEnumConstructor;
import cn.wanfeng.sp.anno.ProtoEnumValue;

/**
 * @date: 2024-12-28 15:26
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public enum BorrowStatus {
    DRAFT(0, "草稿"),

    EXAMINE(1, "审批中"),

    COMPLETE(2, "已完成");

    private Integer value;

    private String desc;

    BorrowStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @ProtoEnumValue
    public Integer getValue(){
        return this.value;
    }

    @ProtoEnumConstructor
    public static BorrowStatus toEnum(Integer value){
        for (BorrowStatus status : values()) {
            if(status.value.equals(value)){
                return status;
            }
        }
        return null;
    }
}
