package cn.wanfeng.sp.util;

import cn.wanfeng.sp.exception.SpException;
import org.slf4j.Logger;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FireworksIDGenerator: 烟花id生成算法.
 * 格式组成为
 *  3位  自定义的对象类型数值（100~921）
 *  10位 秒数偏移量的掩码加密数字
 *  3位  同一秒内的并发序列号（000~999）
 *  3位  随机数
 *
 * @date: 2025-11-22 19:49
 * @author: luozh.wanfeng
 */
public class FireworksIDGenerator {

    private static final Logger log = LogUtil.getSimpleProtoLogger();


    private static final long START_TIME = DateUtils.parseDateTime("2025-11-22 22:00:00").getTime();
    /**
     * 最大秒数偏移量 10位
     */
    private static final long MAX_SECOND_OFFSET = 9999999999L;

    private static final int MAX_SEQ = 999;

    private static final long TIME_MASK = 0b11001010011101010011001110101101L;

    private static final long MAX_TIME_MASK = 0x2540BE3FFL; // 十进制=9999999999

    private static final AtomicInteger sequence = new AtomicInteger(0);

    private static final Random random = new Random();

    /**
     * 上一次生成id的秒数偏移量
     */
    private static long lastSecondOffset = -1;

    /**
     * 生成烟花id
     * @param type 类型数字
     * @return long
     */
    public static long generate_1(int type){
        // 校验类型数字
        if(type < 100 || type > 921){
            throw new SpException("烟花ID生成失败 type = {}, 不在合法范围 100~921 中", type);
        }

        long secondOffset = (System.currentTimeMillis() - START_TIME) / 1000;
        if(secondOffset <= 0){
            throw new SpException("烟花ID生成失败 startTime = {}，开始时间不能大于等于当前时间", type);
        }
        if(secondOffset > MAX_SECOND_OFFSET){
            throw new SpException("烟花ID生成失败 secondOffset = {}，时间偏移量超过最大值 9999999999", secondOffset);
        }

        //若与上次生成的id为同一秒钟，则进行序列号自增
        if(secondOffset == lastSecondOffset){
            int seq = sequence.incrementAndGet();
            if(seq > MAX_SEQ){
                log.warn("烟花ID 时间 {} 已超过最大序列号 {}, 开始轮询等待", secondOffset, MAX_SEQ);
                //超过最大序列号，开启轮询等待至下一秒，并重置序列号
                while (secondOffset == lastSecondOffset){
                    secondOffset = (System.currentTimeMillis() - START_TIME) / 1000;
                }
                sequence.set(0);
            }
        }else {
            sequence.set(0);
            lastSecondOffset = secondOffset;
        }
        int seq = sequence.get();

        // 秒数根据密钥加密：
        long encryptedTime = encryptSecondOffset(secondOffset);
        String timeStr = String.format("%010d", encryptedTime);

        int randomValue = random.nextInt(1000);
        String randomStr = String.format("%03d", randomValue);

        String seqStr = String.format("%03d", seq);

        String idStr = type + timeStr + seqStr + randomStr;
        return Long.parseLong(idStr);
    }

    private static long encryptSecondOffset(long secondOffset){
        // 高17位与低17位异或
        long mixed = secondOffset ^ (secondOffset >>> 17);
        // 与随机掩码异或
        long encrypted = mixed ^ TIME_MASK;
        // 截断到10位十进制范围（0~9999999999）与运算保证数值不超过MAX_TIME_MASK，此处又进行了一次混淆
        return encrypted & MAX_TIME_MASK;
    }


}
