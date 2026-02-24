package cn.wanfeng.sp.cache;


import cn.wanfeng.proto.exception.RedisLockNotGetException;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.util.LogUtil;
import io.lettuce.core.RedisCommandTimeoutException;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @date: 2024-11-10 11:56
 * @author: luozh.wanfeng
 * @description: Redis缓存操作
 * @since: 1.0
 */
@Component
public class CacheOperator {

    private static final Logger log = LogUtil.getSimpleProtoLogger();

    @Resource
    private RedissonClient redissonClient;

    private static final String LOCK_KEY = "Lock";

    /**
     * 设置值
     * @param key key
     * @param value value
     */
    public void set(String key, String value){
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * 设置值、有效期（秒）
     * @param key key
     * @param value value
     * @param expireMillis 有效期（毫秒）
     */
    public void setWithExpire(String key, String value, int expireMillis){
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value, Duration.ofMillis(expireMillis));
    }

    /**
     * 获取值
     * 对超时异常进行重试
     */
    public String get(String key){
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 删除键
     */
    public boolean del(String key){
        return redissonClient.getBucket(key).delete();
    }

    /**
     * 键值自增1
     */
    public Long incr(String key){
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.incrementAndGet();
    }

    /**
     * 键值自增step
     */
    public Long incr(String key, long step){
        if (step <= 0) {
            throw new SpException("Redission incr 步长step必须为正整数");
        }
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.addAndGet(step);
    }

    /**
     * 键值自减1
     */
    public Long decr(String key){
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.decrementAndGet();
    }

    /**
     * 键值自减step
     */
    public Long decr(String key, long step){
        if (step <= 0) {
            throw new SpException("Redission decr 步长step必须为正整数");
        }
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.addAndGet(-step);
    }

    /**
     * 集合添加元素
     */
    public void setAdd(String key, Object... values){
        RSet<Object> set = redissonClient.getSet(key);
        set.addAll(Arrays.stream(values).toList());
    }

    /**
     * 获取集合的所有元素
     */
    public Set<Object> setMembers(String key){
        RSet<Object> set = redissonClient.getSet(key);
        return set.readAll();
    }

    /**
     * 是否为集合内的元素
     */
    public boolean isMember(String key, Object value){
        RSet<Object> set = redissonClient.getSet(key);
        return set.contains(value);
    }

    /**
     * 集合移除元素
     */
    public boolean setRemove(String key, Object value){
        RSet<Object> set = redissonClient.getSet(key);
        return set.remove(value);
    }

    /**
     * 有序集合添加元素、该元素的分数
     */
    public boolean zsetAdd(String key, Object value, double score){
        RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(key);
        return zSet.add(score, value);
    }

    /**
     * 获取分布式锁（阻塞：带等待/超时时间）
     * @param lockKey 锁名
     * @param waitTimeout 获取锁超时时间，毫秒
     * @param leaseTimeout 持有锁超时时间，毫秒（超时自动释放）
     * @return RLock
     */
    public RLock lock(String lockKey, long waitTimeout, long leaseTimeout) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            return lock.tryLock(waitTimeout, leaseTimeout, TimeUnit.MILLISECONDS) ? lock : null;
        } catch (Exception e) {
            log.error("获取Redission锁异常 lockKey={}", lockKey, e);
            throw new SpException(e, "获取Redission锁异常");
        }
    }

    /**
     * 释放分布式锁（安全解锁：校验当前线程持有锁）
     */
    public void unlock(RLock lock) {
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
