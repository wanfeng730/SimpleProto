package cn.wanfeng.sp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ArrayUtil: desc.
 * @date: 2025-12-17 11:56
 * @author: luozh.wanfeng
 */
public class ArrayUtil {

    /**
     * 转成列表，使用原生ArrayList，可修改
     * @param elements 数组
     * @return ArrayList
     * @param <E> 类型
     */
    public static <E> List<E> asList(E... elements){
        ArrayList<E> list = new ArrayList<>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }

}
