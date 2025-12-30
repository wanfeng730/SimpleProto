package cn.wanfeng.sp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    /**
     * 列表转换为字符串，指定分隔符
     * @param arr 列表
     * @param separator 分隔符
     * @return String
     */
    public static String toStringWithSep(Integer[] arr, String separator){
        if(cn.hutool.core.util.ArrayUtil.isEmpty(arr) || Objects.isNull(separator)){
            return null;
        }
        return String.join(separator, Arrays.stream(arr).map(Object::toString).toList());
    }
}
