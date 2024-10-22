package cn.wanfeng.sp.base;

import cn.wanfeng.proto.exception.SpException;
import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.base.domain.ISpBaseObject;
import cn.wanfeng.sp.base.object.SpSession;
import cn.wanfeng.sp.base.object.SpSettingsDO;
import cn.wanfeng.sp.common.BusinessTypeConstant;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @date: 2024-06-21 11:06
 * @author: luozh
 * @description: 基础对象测试
 */
public class SpBaseObjectTest extends SimpleprotoApplicationTest {

    @Resource
    private SpSession spSession;

    @Test
    public void test_SpBaseObject_borrowForm() {
        BorrowForm borrowForm = new BorrowForm(spSession, "A002", 222, new Date());
        Assertions.assertNotNull(borrowForm);
        Assertions.assertEquals(borrowForm.getType(), BusinessTypeConstant.BORROW_FORM);
        Assertions.assertNotNull(borrowForm.getName());
        Assertions.assertNotNull(borrowForm.getCreateDate());
        Assertions.assertNotNull(borrowForm.getModifyDate());
        Assertions.assertFalse(borrowForm.getDelete());

        long step0 = System.currentTimeMillis();
        borrowForm.store();
        LogUtils.debug("SpBaseObject对象新建保存耗时{}ms", System.currentTimeMillis() - step0);

        Assertions.assertNotNull(borrowForm.getId());

        SpSettingsDO spSettingsDO = spSession.settingsStorage().findByName(ISpBaseObject.BASE_OBJECT_ID_INCREASE_NAME);
        Long maxBaseObjectId = spSettingsDO.getIncreaseLong();
        borrowForm = new BorrowForm(spSession, maxBaseObjectId);
        Assertions.assertEquals(BusinessTypeConstant.BORROW_FORM, borrowForm.getType());
        Assertions.assertNotNull(borrowForm.getName());
        Assertions.assertEquals(false, borrowForm.getDelete());

        borrowForm.setName("借阅单1");
        borrowForm.updateApplyDays(43);

        long step1 = System.currentTimeMillis();
        borrowForm.store();
        LogUtils.debug("SpBaseObject对象更新保存耗时{}ms", System.currentTimeMillis() - step1);

        borrowForm = new BorrowForm(spSession, maxBaseObjectId);
        Assertions.assertNotNull(borrowForm);
        Assertions.assertEquals("借阅单1", borrowForm.getName());
        Assertions.assertEquals(43, borrowForm.getApplyDays());
        Assertions.assertTrue(borrowForm.getModifyDate().after(borrowForm.getCreateDate()));

        long step2 = System.currentTimeMillis();
        borrowForm.remove();
        LogUtils.debug("SpBaseObject对象删除耗时{}ms", System.currentTimeMillis() - step2);

        Assertions.assertThrows(SpException.class, () -> new BorrowForm(spSession, maxBaseObjectId));

        Assertions.assertThrows(SpException.class, () -> new BorrowForm(spSession, null));

        LogUtils.info("SpBaseObject功能测试完成");
    }

    @Test
    public void test_mybatis_interceptor() {
        SpSettingsDO spSettingsDO = spSession.settingsStorage().findByName("SpBaseObjectId");
        Assertions.assertNotNull(spSettingsDO);
    }
}
