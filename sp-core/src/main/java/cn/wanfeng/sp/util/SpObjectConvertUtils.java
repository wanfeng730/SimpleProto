package cn.wanfeng.sp.util;


import cn.wanfeng.sp.api.base.domain.SpBaseObject;
import cn.wanfeng.sp.api.base.object.SpBaseObjectDO;

/**
 * @date: 2024-10-20 21:15
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public class SpObjectConvertUtils {

    public static SpBaseObjectDO convertSpBaseObjectToDO(SpBaseObject baseObject, byte[] data) {
        SpBaseObjectDO spBaseObjectDO = new SpBaseObjectDO();
        spBaseObjectDO.setId(baseObject.getId());
        spBaseObjectDO.setType(baseObject.getType());
        spBaseObjectDO.setName(baseObject.getName());
        spBaseObjectDO.setCreateDate(baseObject.getCreateDate());
        spBaseObjectDO.setModifyDate(baseObject.getModifyDate());
        spBaseObjectDO.setIsDelete(baseObject.getDelete());
        spBaseObjectDO.setData(data);
        return spBaseObjectDO;
    }

    public static SpBaseObjectDO convertSpBaseObjectToDO(SpBaseObject spBaseObject) {
        return convertSpBaseObjectToDO(spBaseObject, null);
    }

}
