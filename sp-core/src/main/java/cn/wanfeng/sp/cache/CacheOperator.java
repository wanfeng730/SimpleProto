package cn.wanfeng.sp.cache;


import cn.wanfeng.proto.exception.RedisLockNotGetException;
import cn.wanfeng.proto.exception.SpException;
import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.config.SimpleProtoConfig;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @date: 2024-11-10 11:56
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Component
public class CacheOperator {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LOCK_KEY = "Lock";

    /**
     * 设置值
     */
    public void set(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取值
     */
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除键
     */
    public void del(String key){
        redisTemplate.delete(key);
    }

    /**
     * 键值自增1
     */
    public Long incr(String key){
        return incr(key, 1);
    }

    /**
     * 键值自增step
     */
    public Long incr(String key, long step){
        return redisTemplate.opsForValue().increment(key, step);
    }

    /**
     * 键值自减1
     */
    public Long decr(String key){
        return decr(key, 1);
    }

    /**
     * 键值自减step
     */
    public Long decr(String key, long step){
        return redisTemplate.opsForValue().decrement(key, step);
    }

    /**
     * 列表（双向队列）左侧添加元素
     */
    public void listLeftPush(String key, Object value){
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 列表（双向队列）右侧添加元素
     */
    public void listRightPush(String key, Object value){
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 列表（双向队列）左侧弹出元素
     */
    public Object listLeftPop(String key){
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 列表（双向队列）右侧弹出元素
     */
    public Object listRightPop(String key){
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 集合添加元素
     */
    public void setAdd(String key, Object... values){
        redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取集合的所有元素
     */
    public Set<Object> setMembers(String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 是否为集合内的元素
     */
    public Boolean isMember(String key, Object value){
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 集合移除元素
     */
    public void setRemove(String key, Object value){
        redisTemplate.opsForSet().remove(key, value);
    }

    /**
     * 有序集合添加元素、该元素的分数
     */
    public void zsetAdd(String key, Object value, double score){
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 有序集合获取指定位置的元素(包含start位置和end位置的元素）
     */
    public Set<Object> zsetRangeByIndex(String key, long start, long end){
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 有序集合获取指定分数范围元素（包含minScore分数和maxScore分数的元素）
     */
    public Set<Object> zsetRangeByScore(String key, double minScore, double maxScore){
        return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
    }

    /**
     *有序集合指定元素增加分数
     */
    public Double zsetIncrScore(String key, Object value, double incrScore){
        return redisTemplate.opsForZSet().incrementScore(key, value, incrScore);
    }

    /**
     * 有序集合统计分数范围的元素个数
     */
    public Long zsetCountByScore(String key, double minScore, double maxScore){
        return redisTemplate.opsForZSet().count(key, minScore, maxScore);
    }

    /**
     * 有序集合元素的排名（升序，第一名返回的是0）
     */
    public Long zsetRankAsc(String key, Object value){
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 有序集合元素的排名（降序，第一名返回的是0）
     */
    public Long zsetRankDesc(String key, Object value){
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 有序集合根据位置删除元素
     */
    public void zsetRemoveByIndex(String key, long start, long end){
        redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 有序集合根据分数范围删除元素
     */
    public void zsetRemoveByScore(String key, double minScore, double maxScore){
        redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);

    }

    /**
     * 加锁（永久）
     * @param lockName 锁名称
     * @return 是否加锁成功
     */
    public boolean lock(@NotBlank String lockName){
        if(StringUtils.isBlank(lockName)){
            throw new SpException("Cache Lock Failed, [lockName] must Not Blank!");
        }
        String lockKey = generateThisAppLockKey(lockName);
        String lockValue = generateThisAppLockValue(lockName);
        return Optional.ofNullable(
                redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue)
        ).orElse(false);
    }

    @Retryable(retryFor = RedisLockNotGetException.class, maxAttempts = 300, backoff = @Backoff(delay = 100))
    public boolean lockRetryable(@NotBlank String lockName){
        boolean locked = lock(lockName);
        if(!locked){
            throw new RedisLockNotGetException();
        }
        return true;
    }


    /**
     * 加锁
     * @param lockName 锁名称
     * @param expireSeconds 锁的有效时间（秒）
     * @return 是否加锁成功
     */
    public boolean lock(@NotBlank String lockName, @NotNull long expireSeconds){
        String lockKey = generateThisAppLockKey(lockName);
        String lockValue = generateThisAppLockValue(lockName);
        return Optional.ofNullable(
                redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expireSeconds, TimeUnit.SECONDS)
        ).orElse(false);
    }

    /**
     * 释放锁
     * @param lockName 锁名称
     */
    public void unLock(@NotBlank String lockName){
        String lockKey = generateThisAppLockKey(lockName);
        Boolean success = Optional.ofNullable(
                redisTemplate.delete(lockKey)
        ).orElse(false);
        if(success){
            LogUtils.debug("锁[{}]释放成功", lockKey);
        }
    }


    // @应用名称:Lock:锁名称
    private static String generateThisAppLockKey(String lockName){
        return "@" + String.join(":", SimpleProtoConfig.appName, LOCK_KEY, lockName);
    }

    private static String generateThisAppLockValue(String lockName){
        return String.format("The Lock[%s] has been Used", lockName);
    }

    /**
     * 等待半秒
     */
    private static void sleepHalfSecond(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
