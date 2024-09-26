package cn.wanfeng.sp.util.obj;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date: 2024-08-25 22:05
 * @author: luozh
 * @description: 一个Map用两个Key映射一个Value，新奇！
 * @since: 1.0
 */
public class DoubleKeyMap<K1, K2, V> {

    private Map<DoubleKey, V> storageMap;


    public DoubleKeyMap() {
        this.storageMap = new ConcurrentHashMap<>(4);
    }

    public void put(K1 key1, K2 key2, V value) {
        DoubleKey doubleKey = new DoubleKey(key1, key2);
        this.storageMap.put(doubleKey, value);
    }

    public V get(K1 key1, K2 key2) {
        return this.storageMap.get(buildDoubleKey(key1, key2));
    }

    public Set<V> getByKey1(K1 key1) {
        // LzhTODO: 通过key1获取所有值的集合
        return null;
    }

    public Set<V> getByKey2(K2 key2) {
        // LzhTODO: 通过key2获取所有值的集合
        return null;
    }

    private DoubleKey buildDoubleKey(K1 key1, K2 key2) {
        return new DoubleKey(key1, key2);
    }

    private class DoubleKey {
        private K1 key1;
        private K2 key2;

        DoubleKey(K1 key1, K2 key2) {
            this.key1 = key1;
            this.key2 = key2;
        }

        public K1 getKey1() {
            return key1;
        }

        public K2 getKey2() {
            return key2;
        }

        /**
         * // LzhTODO: 尽量减少hash值冲突的方法
         *
         * @return hashCode
         */
        @Override
        public int hashCode() {
            return key1.hashCode() * 100 + key2.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public String toString() {
            return key1.toString() + "-" + key2.toString();
        }
    }

    public class DoubleKeyEntry<K1, K2, V> {
        private K1 key1;
        private K2 key2;
        private V value;

        public DoubleKeyEntry(K1 key1, K2 key2, V value) {
            this.key1 = key1;
            this.key2 = key2;
            this.value = value;
        }

        @Override
        public String toString() {
            return "DoubleKeyEntry{" +
                    "key1=" + key1 +
                    ", key2=" + key2 +
                    ", value=" + value +
                    '}';
        }
    }


}
