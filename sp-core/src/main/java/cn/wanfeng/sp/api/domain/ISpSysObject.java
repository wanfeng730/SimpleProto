package cn.wanfeng.sp.api.domain;

import cn.wanfeng.sp.api.enums.SystemTag;

/**
 * @date: 2024-12-08 16:33
 * @author: luozh.wanfeng
 * @description: 系统对象抽象接口
 * @since: 1.0
 */
public interface ISpSysObject extends ISpBaseObject {

    String TAG_FIELD = "tag";
    String PATH_FIELD = "path";
    String PARENT_ID_FIELD = "parent_id";
    String PARENT_PATH_FIELD ="parent_path";

    int TAG_INDEX = 1007;
    int PATH_INDEX = 1008;
    int PARENT_ID_INDEX = 1009;
    int PARENT_PATH_INDEX = 1010;

    String pathSeparator = "/";

    /**
     * 获取该对象的系统类型
     */
    String getTag();

    /**
     * 获取该对象的业务路径
     */
    String getPath();

    /**
     * 获取该对象的父id
     */
    Long getParentId();

    /**
     * 获取该对象的父路径
     */
    String getParentPath();

    /**
     * 设置系统类型
     */
    void setTag(SystemTag systemTag);



    /**
     * 将该对象移动到父id的对象下
     */
    void move(Long parentId);

    /**
     * 将该对象移动到父路径的对象下
     */
    void move(String parentPath);

    /**
     * 将该对象移动到父对象下
     */
    void move(ISpSysObject parentSysObject);




}
