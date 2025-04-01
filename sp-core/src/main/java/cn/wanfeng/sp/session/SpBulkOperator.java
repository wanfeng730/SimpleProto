package cn.wanfeng.sp.session;

import cn.wanfeng.sp.api.dataobject.SpBaseObjectDO;
import cn.wanfeng.sp.api.domain.ISpBaseObject;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.Resource;
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
     * 批量新建基础对象
     * @param baseObjectList 基础对象列表
     */
    public void bulkCreateBaseObject(List<ISpBaseObject> baseObjectList){
        List<SpBaseObjectDO> objectDOList = new ArrayList<>();
        List<Map<String, Object>> documentList = new ArrayList<>();
        for (ISpBaseObject baseObject : baseObjectList) {
            objectDOList.add(baseObject.generateBaseObjectDO());
            documentList.add(baseObject.getDocument());
        }
        try {
            session.bulkCreateBaseObjectToStorage(objectDOList, documentList);
        } catch (IOException e) {
            LogUtil.error("批量新建数据保存失败，事务已回滚", e);
        }
    }
}
