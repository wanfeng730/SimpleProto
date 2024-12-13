package cn.wanfeng.sp.util;

import cn.wanfeng.sp.api.base.domain.SpBaseObject;
import cn.wanfeng.sp.api.base.object.SpBaseObjectDO;
import cn.wanfeng.sp.api.sys.domain.SpSysObject;
import cn.wanfeng.sp.api.sys.object.SpSysObjectDO;

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

    public static SpSysObjectDO convertSpSysObjectToDO(SpSysObject sysObject, byte[] data){
        SpSysObjectDO sysObjectDO = new SpSysObjectDO();
        sysObjectDO.setId(sysObject.getId());
        sysObjectDO.setType(sysObject.getType());
        sysObjectDO.setName(sysObject.getName());
        sysObjectDO.setCreateDate(sysObject.getCreateDate());
        sysObjectDO.setModifyDate(sysObject.getModifyDate());
        sysObjectDO.setIsDelete(sysObject.getDelete());
        sysObjectDO.setTag(sysObject.getTag());
        sysObjectDO.setPath(sysObject.getPath());
        sysObjectDO.setParentId(sysObject.getParentId());
        sysObjectDO.setParentPath(sysObject.getParentPath());
        sysObjectDO.setData(data);
        return sysObjectDO;
    }

    public static SpBaseObjectDO convertSpBaseObjectToDO(SpBaseObject spBaseObject) {
        return convertSpBaseObjectToDO(spBaseObject, null);
    }

}
