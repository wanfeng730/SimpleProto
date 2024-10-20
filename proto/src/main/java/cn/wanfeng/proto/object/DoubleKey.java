package cn.wanfeng.proto.object;


import java.util.Objects;

/**
 * @date: 2024-10-19 22:45
 * @author: luozh.wanfeng
 * @description: two key as map key
 * @since: 1.0
 */
public class DoubleKey<K1, K2> {

    public K1 key1;
    public K2 key2;

    public DoubleKey(K1 key1, K2 key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoubleKey)) {
            return false;
        }

        DoubleKey dk = (DoubleKey) o;
        if (!Objects.equals(key1, dk.key1)) {
            return false;
        }
        if (!Objects.equals(key2, dk.key2)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = key1 != null ? key1.hashCode() : 0;
        result = 31 * result + (key2 != null ? key2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "[" + key1 + ", " + key2 + "]";
    }

}
