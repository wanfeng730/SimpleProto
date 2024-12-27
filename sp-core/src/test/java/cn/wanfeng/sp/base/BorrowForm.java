package cn.wanfeng.sp.base;


import cn.wanfeng.sp.anno.ProtoField;
import cn.wanfeng.sp.anno.Type;
import cn.wanfeng.sp.api.domain.SpBaseObject;
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

    /**
     * ProtoField declare index and name of this property
     * [index] is used to build serialized data and store in database,
     * [index] is unique in a Class
     * [name] is used to build document and store in opensearch,
     * [name] is unique in a Class
     */
    @ProtoField(index = 1, name = "form_no")
    private String formNo;

    @ProtoField(index = 2, name = "apply_days")
    private Integer applyDays;

    @ProtoField(index = 3, name = "expire_date")
    private Date expireDate;

    /**
     * Constructor for Create Object
     */
    public BorrowForm(SpSession session, String formNo, Integer applyDays, Date expireDate) {
        super(session, BusinessTypeConstant.BORROW_FORM);
        this.formNo = formNo;
        this.applyDays = applyDays;
        this.expireDate = expireDate;
    }

    /**
     * Constructor for Get Object
     */
    public BorrowForm(SpSession session, Long id) {
        super(session, id);
    }

    public void updateApplyDays(Integer applyDays) {
        this.applyDays = applyDays;
    }


}
