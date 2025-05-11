package cn.wanfeng.sp.util;

import cn.wanfeng.sp.api.dataobject.SpDataObjectDO;
import cn.wanfeng.sp.api.domain.SpBaseObject;
import cn.wanfeng.sp.api.domain.SpSysObject;

/**
 * @date: 2024-10-20 21:15
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public class SpObjectConvertUtils {

    public static SpDataObjectDO convertSpBaseObjectToDO(SpBaseObject baseObject, byte[] data) {
        SpDataObjectDO spBaseObjectDO = new SpDataObjectDO();
        spBaseObjectDO.setId(baseObject.getId());
        spBaseObjectDO.setType(baseObject.getType());
        spBaseObjectDO.setName(baseObject.getName());
        spBaseObjectDO.setCreateDate(baseObject.getCreateDate());
        spBaseObjectDO.setModifyDate(baseObject.getModifyDate());
        spBaseObjectDO.setIsDelete(baseObject.getDelete());
        spBaseObjectDO.setData(data);
        return spBaseObjectDO;
    }

    public static SpDataObjectDO convertSpSysObjectToDO(SpSysObject sysObject, byte[] data){
        SpDataObjectDO sysObjectDO = new SpDataObjectDO();
        sysObjectDO.setId(sysObject.getId());
        sysObjectDO.setType(sysObject.getType());
        sysObjectDO.setName(sysObject.getName());
        sysObjectDO.setCreateDate(sysObject.getCreateDate());
        sysObjectDO.setModifyDate(sysObject.getModifyDate());
        sysObjectDO.setIsDelete(sysObject.getDelete());
        sysObjectDO.setTag(sysObject.getSystemTag());
        sysObjectDO.setPath(sysObject.getPath());
        sysObjectDO.setParentId(sysObject.getParentId());
        sysObjectDO.setParentPath(sysObject.getParentPath());
        sysObjectDO.setData(data);
        return sysObjectDO;
    }

}
