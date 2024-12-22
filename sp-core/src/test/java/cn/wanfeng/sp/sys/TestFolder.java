package cn.wanfeng.sp.sys;


import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.anno.Type;
import cn.wanfeng.sp.api.domain.SpFolder;
import cn.wanfeng.sp.common.BusinessTypeConstant;
import cn.wanfeng.sp.session.SpSession;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @date: 2024-12-09 22:03
 * @author: luozh.wanfeng
 * @description: SpSysObject测试
 * @since: 1.0
 */
@Getter
@Setter
@Type(BusinessTypeConstant.TEST_FOLDER)
public class TestFolder extends SpFolder {

    @ProtoField(index = 1, name = "display_name")
    private String displayName;

    @ProtoField(index = 2, name = "code")
    private String code;

    @ProtoField(index = 3, name = "expire_date")
    private Date expireDate;


    public TestFolder(SpSession session, String name, String displayName, String code, Date expireDate) {
        super(session, BusinessTypeConstant.TEST_FOLDER, name);
        this.displayName = displayName;
        this.code = code;
        this.expireDate = expireDate;
    }

    public TestFolder(SpSession session, Long id) {
        super(session, id);
    }
}
