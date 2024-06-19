package cn.wanfeng.sp.object.base;

/**
 * @date: 2024-04-02 23:48
 * @author: luozh
 * @since: 1.0
 */
public interface ISpBaseObject {
    /**
     * 将对象持久化
     */
    void store();

    /**
     * 将对象从持久化删除
     */
    void remove();
}
