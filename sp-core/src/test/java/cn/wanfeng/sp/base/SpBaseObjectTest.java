package cn.wanfeng.sp.base;

import cn.hutool.core.util.RandomUtil;
import cn.wanfeng.proto.exception.SpException;
import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.base.domain.ISpBaseObject;
import cn.wanfeng.sp.base.mapper.es.BorrowFormMapper;
import cn.wanfeng.sp.base.object.SpSettingsDO;
import cn.wanfeng.sp.common.BusinessTypeConstant;
import cn.wanfeng.sp.config.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.ThreadPoolTemplateUtils;
import jakarta.annotation.Resource;
import lombok.Synchronized;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @date: 2024-06-21 11:06
 * @author: luozh
 * @description: 基础对象测试
 */
public class SpBaseObjectTest extends SimpleprotoApplicationTest {

    @Resource
    private SpSession spSession;

    @Resource
    private BorrowFormMapper borrowFormMapper;

    @Test
    public void test_opendistro(){

    }

    @Test
    public void test_SpBaseObject_createBorrowForm() {


        BorrowForm borrowForm = new BorrowForm(spSession, "A002", 222, new Date());
        Assertions.assertNotNull(borrowForm);
        Assertions.assertEquals(borrowForm.getType(), BusinessTypeConstant.BORROW_FORM);
        Assertions.assertNotNull(borrowForm.getName());
        Assertions.assertNotNull(borrowForm.getCreateDate());
        Assertions.assertNotNull(borrowForm.getModifyDate());
        Assertions.assertFalse(borrowForm.getDelete());

        long step0 = System.currentTimeMillis();
        borrowForm.store();
        LogUtils.info("SpBaseObject对象新建保存耗时{}ms", System.currentTimeMillis() - step0);

        Assertions.assertNotNull(borrowForm.getId());

        SpSettingsDO spSettingsDO = spSession.databaseStorage().findSettingsByName(SimpleProtoConfig.settingsTable, ISpBaseObject.OBJECT_ID_INCREASE_NAME);
        Long maxBaseObjectId = spSettingsDO.getIncreaseLong();

        borrowForm = new BorrowForm(spSession, maxBaseObjectId);
        Assertions.assertEquals(BusinessTypeConstant.BORROW_FORM, borrowForm.getType());
        Assertions.assertNotNull(borrowForm.getName());
        Assertions.assertEquals(false, borrowForm.getDelete());

        borrowForm.setName("借阅单1");
        borrowForm.updateApplyDays(43);

        long step1 = System.currentTimeMillis();
        borrowForm.store();
        LogUtils.info("SpBaseObject对象更新保存耗时{}ms", System.currentTimeMillis() - step1);
    }

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
        LogUtils.info("SpBaseObject对象新建保存耗时{}ms", System.currentTimeMillis() - step0);

        Assertions.assertNotNull(borrowForm.getId());

        SpSettingsDO spSettingsDO = spSession.databaseStorage().findSettingsByName(SimpleProtoConfig.settingsTable, ISpBaseObject.OBJECT_ID_INCREASE_NAME);
        Long maxBaseObjectId = spSettingsDO.getIncreaseLong();

        List<BorrowFormDO> borrowFormDOList = borrowFormMapper.findById(maxBaseObjectId);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(borrowFormDOList));

        borrowForm = new BorrowForm(spSession, maxBaseObjectId);
        Assertions.assertEquals(BusinessTypeConstant.BORROW_FORM, borrowForm.getType());
        Assertions.assertNotNull(borrowForm.getName());
        Assertions.assertEquals(false, borrowForm.getDelete());

        borrowForm.setName("借阅单1");
        borrowForm.updateApplyDays(43);

        long step1 = System.currentTimeMillis();
        borrowForm.store();
        LogUtils.info("SpBaseObject对象更新保存耗时{}ms", System.currentTimeMillis() - step1);

        borrowForm = new BorrowForm(spSession, maxBaseObjectId);
        Assertions.assertNotNull(borrowForm);
        Assertions.assertEquals("借阅单1", borrowForm.getName());
        Assertions.assertEquals(43, borrowForm.getApplyDays());
        Assertions.assertTrue(borrowForm.getModifyDate().after(borrowForm.getCreateDate()));

        long step2 = System.currentTimeMillis();
        borrowForm.remove();
        LogUtils.info("SpBaseObject对象删除耗时{}ms", System.currentTimeMillis() - step2);

        Assertions.assertThrows(SpException.class, () -> new BorrowForm(spSession, maxBaseObjectId));

        Assertions.assertThrows(SpException.class, () -> new BorrowForm(spSession, null));

        LogUtils.info("SpBaseObject功能测试完成");
    }



    /**
     * 2秒中内随机时间并发100个创建数据的请求，执行情况：
     * 设置表自增id：执行前741，执行后841
     * 数据表数量：执行前737，执行后837
     * es数据数量：执行前737，执行后837
     * 总计运行时间：43.571s
     * 分析：锁获取重试间隔较大
     */
    @Test
    public void testManyBorrowFormAsyncCreate() throws InterruptedException {
        //由于测试用例的主线程，必须在程序末尾阻塞等待异步方法执行完，否则主线程销毁后ThreadPool也不会执行
        CountDownLatch countDownLatch = new CountDownLatch(100);

        ThreadPoolTaskExecutor executor = ThreadPoolTemplateUtils.createExecutor(100);
        executor.initialize();
        long step0 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            String formNo = String.format("A%06d", i+1);
            Date expireDate = new Date(System.currentTimeMillis() + 8 * 3600 * 1000);
            executor.execute(() -> {
                try {
                    Integer delay = randomSeconds();
                    LogUtils.info("{}毫秒后执行", delay);
                    Thread.sleep(delay);

                    long start = System.currentTimeMillis();
                    BorrowForm borrowForm = new BorrowForm(spSession, formNo, 77, expireDate);
                    borrowForm.store();
                    LogUtils.info("借阅单[{}]创建成功，耗时={}，剩余数量={}\n", borrowForm.getFormNo(), System.currentTimeMillis() - start, countDownLatch.getCount());

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    syncCountDown(countDownLatch);
                }
            });
        }

        countDownLatch.await();
        LogUtils.info("SpBaseObject并发测试完成, 总耗时{}s", (System.currentTimeMillis() - step0) / 1000.0);
    }

    private Integer randomSeconds(){
        return RandomUtil.randomInt(2000, 3000);
    }

    @Synchronized
    private void syncCountDown(CountDownLatch latch){
        latch.countDown();
    }


}
