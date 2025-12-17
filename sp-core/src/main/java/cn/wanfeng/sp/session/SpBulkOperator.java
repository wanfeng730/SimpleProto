package cn.wanfeng.sp.session;

import cn.wanfeng.sp.api.dataobject.SpDataObjectDO;
import cn.wanfeng.sp.api.domain.ISpBaseObject;
import cn.wanfeng.sp.api.domain.ISpFile;
import cn.wanfeng.sp.api.model.SpPropertyValue;
import cn.wanfeng.sp.exception.SimpleExceptionCode;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.util.LogUtil;
import cn.wanfeng.sp.util.ThreadUtil;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SpBulkOperator: 批量操作.
 *
 * @date: 2025-04-01 17:27
 * @author: luozh.wanfeng
 */
@Component
public class SpBulkOperator {

    @Resource
    private SpSession session;

    /**
     * 批量保存（新建、更新）基础对象，分为两种情况：
     * 1. 批量创建
     * 2. 批量更新
     *
     * OpenSearch批量操作可能会有延迟，导致后续业务没法获取到更新后的数据，使用该方法在批量操作执行完成后等待一会儿
     * 默认等待 1100ms
     * 默认情况下，写入操作会先写入内存缓冲区，每隔 1 秒才会刷新到分段（Segment）供查询可见，批量写入也遵循这一机制。
     * 通过修改refresh参数，有解决数据查询不到的三种方案
     *      true：写入后立即刷新索引
     *      wait_for: 等待当前写入的分片完成刷新（不立即刷新，而是等待已有刷新周期，性能更优）
     *      false: 不刷新（默认值）
     * @param baseObjectList 基础对象列表
     */
    public void bulkStore(List<? extends ISpBaseObject> baseObjectList){

        List<ISpBaseObject> createList = new ArrayList<>();
        List<ISpBaseObject> updateList = new ArrayList<>();

        // 筛选分为四个部分
        for (ISpBaseObject object : baseObjectList) {
            assertObjectSupportBulkOperation(object);
            if(object.isNewObject()){
                createList.add(object);
            }else {
                updateList.add(object);
            }
        }


        if(CollectionUtils.isNotEmpty(createList)){
            bulkCreateObject(createList);
        }

        if(CollectionUtils.isNotEmpty(updateList)){
            bulkUpdateObject(updateList);
        }

    }

    public void bulkRemove(List<? extends ISpBaseObject> objectList){
        List<Long> idList = new ArrayList<>();
        for (ISpBaseObject object : objectList) {
            assertObjectSupportBulkOperation(object);
            if(object.isNewObject()){
                continue;
            }
            idList.add(object.getId());
        }

        if(CollectionUtils.isNotEmpty(idList)){
            // @luozh-code: 设置每批次最大200条执行，防止sql过长
            try {
                session.bulkRemoveObjectToStorage(idList);
            } catch (IOException e) {
                LogUtil.error("批量新建数据保存失败，事务已回滚", e);
            }
        }

    }

    private void bulkCreateObject(List<? extends ISpBaseObject> baseObjectList){
        List<SpDataObjectDO> objectDOList = new ArrayList<>();
        List<Map<String, SpPropertyValue>> propertyValueContainerList = new ArrayList<>();
        for (ISpBaseObject baseObject : baseObjectList) {
            // 保存对象前执行前置操作，生成数据
            baseObject.beforeStoreOperations();
            // 获取保存数据库存储数据
            objectDOList.add(baseObject.generateDataObjectDO());
            // 获取高级搜索存储数据
            propertyValueContainerList.add(baseObject.getDocument());
        }
        // @luozh-code: 设置每批次最大200条执行，防止sql过长
        try {
            session.bulkCreateObjectToStorage(objectDOList, propertyValueContainerList);
        } catch (IOException e) {
            LogUtil.error("批量新建数据保存失败，事务已回滚", e);
        }
        // 后置操作
        for (ISpBaseObject baseObject : baseObjectList) {
            baseObject.afterStoreOperations();
        }
    }

    private void bulkUpdateObject(List<? extends ISpBaseObject> baseObjectList){
        List<SpDataObjectDO> objectDOList = new ArrayList<>();
        List<Map<String, SpPropertyValue>> propertyValueContainerList = new ArrayList<>();
        for (ISpBaseObject baseObject : baseObjectList) {
            // 保存对象前执行前置操作，生成数据
            baseObject.beforeStoreOperations();
            // 获取保存数据库存储数据
            objectDOList.add(baseObject.generateDataObjectDO());
            // 获取高级搜索存储数据
            propertyValueContainerList.add(baseObject.getDocument());
        }
        // @luozh-code: 设置每批次最大200条执行，防止sql过长
        try {
            session.bulkUpdateObjectToStorage(objectDOList, propertyValueContainerList);
        } catch (IOException e) {
            LogUtil.error("批量更新数据保存失败，事务已回滚", e);
        }
        // 后置操作
        for (ISpBaseObject baseObject : baseObjectList) {
            baseObject.afterStoreOperations();
        }
    }

    private void assertObjectSupportBulkOperation(ISpBaseObject object){
        if(object instanceof ISpFile){
            throw new SpException(SimpleExceptionCode.BULK_STORE_NOT_SUPPORT_FILE_OBJECT);
        }
    }

    @Deprecated
    private static void waitOpenSearchCompleteBulkRequest() {
        ThreadUtil.sleepQuietly(1100);
    }


}
