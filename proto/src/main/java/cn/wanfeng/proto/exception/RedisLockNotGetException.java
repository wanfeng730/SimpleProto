package cn.wanfeng.proto.exception;


/**
 * @date: 2024-11-12 21:13
 * @author: luozh.wanfeng
 * @description: redis锁获取失败异常
 * @since: 1.0
 */
public class RedisLockNotGetException extends RuntimeException{

    public RedisLockNotGetException(){
        super("未获取到Redis锁");
    }

    public RedisLockNotGetException(String message) {
        super(message);
    }

    public RedisLockNotGetException(Throwable cause) {
        super(cause);
    }

    public RedisLockNotGetException(String message, Throwable cause) {
        super(message, cause);
    }
}
