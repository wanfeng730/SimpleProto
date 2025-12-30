package cn.wanfeng.sp.util;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @date: 2024-12-22 12:24
 * @author: luozh.wanfeng
 * @description: 简单的集合工具类，提供列表分组功能
 */
public class ListUtil {

    /**
     * 列表分组
     *
     * @param list     列表
     * @param subListSize 每组元素的数量
     * @return 分组列表
     */
    public static <T> List<List<T>> partitionByItemSize(@NotNull List<T> list, @Min(1) int subListSize){
        List<List<T>> partitionList = new ArrayList<>();
        // partition cursor
        int pc = 0;

        for (T t : list) {
            //指针大于了列表长度，需要增加一个列表
            if(partitionList.size() - 1 < pc){
                partitionList.add(new ArrayList<>());
            }
            List<T> partition = partitionList.get(pc);
            partition.add(t);
            //若该partition已满，指针+1，到下一个列表中添加
            if(partition.size() == subListSize){
                pc++;
            }
        }
        return partitionList;
    }


    /**
     * 列表分组
     * @param list 列表
     * @param subListCount 组的数量
     * @return 分组列表
     */
    public static <T> List<List<T>> partitionByItemCount(List<T> list, int subListCount){
        List<List<T>> partitionList = new ArrayList<>();
        for (int i = 0; i < subListCount; i++) {
            partitionList.add(new ArrayList<>());
        }
        int pc = 0;
        for (T t : list) {
            partitionList.get(pc).add(t);
            pc = (pc + 1) % subListCount;
        }
        return partitionList;
    }

    /**
     * 列表转换为字符串，指定分隔符
     * @param list 列表
     * @param separator 分隔符
     * @return String
     */
    public static String toStringWithSep(List<Integer> list, String separator){
        if(CollectionUtils.isEmpty(list) || Objects.isNull(separator)){
            return null;
        }
        return String.join(separator, list.stream().map(Object::toString).toList());
    }
}
