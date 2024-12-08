package cn.wanfeng.sp.base;


import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.anno.Type;
import cn.wanfeng.sp.api.base.domain.SpBaseObject;
import cn.wanfeng.sp.common.BusinessTypeConstant;
import cn.wanfeng.sp.session.SpSession;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @date: 2024-10-20 14:41
 * @author: luozh.wanfeng
 * @description: 测试SpBaseObject
 * @since: 1.0
 */
@Type(BusinessTypeConstant.BORROW_FORM)
@Getter
@Setter
public class BorrowForm extends SpBaseObject {

    @ProtoField(index = 1, name = "form_no")
    private String formNo;

    @ProtoField(index = 2, name = "apply_days")
    private Integer applyDays;

    @ProtoField(index = 3, name = "expire_date")
    private Date expireDate;

    public BorrowForm(SpSession session, String formNo, Integer applyDays, Date expireDate) {
        super(session, BusinessTypeConstant.BORROW_FORM);
        this.formNo = formNo;
        this.applyDays = applyDays;
        this.expireDate = expireDate;
    }

    public BorrowForm(SpSession session, Long id) {
        super(session, id);
    }

    public void updateApplyDays(Integer applyDays) {
        this.applyDays = applyDays;
    }


}
