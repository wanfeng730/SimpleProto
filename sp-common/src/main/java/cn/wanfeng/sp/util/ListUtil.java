package cn.wanfeng.sp.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ListUtil: desc.
 *
 * @date: 2025-10-30 16:10
 * @author: luozh.wanfeng
 */
public class ListUtil {

    public static <E> ArrayList<E> newArrayList(E... elements){
        ArrayList<E> list = new ArrayList<>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }
}
