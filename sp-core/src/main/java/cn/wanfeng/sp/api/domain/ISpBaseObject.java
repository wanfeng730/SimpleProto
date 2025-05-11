package cn.wanfeng.sp.api.domain;

import cn.wanfeng.sp.api.dataobject.SpDataObjectDO;

import java.util.Date;
import java.util.Map;

/**
 * @date: 2024-04-02 23:48
 * @author: luozh
 * @since: 1.0
 */
public interface ISpBaseObject {
    String ID_FIELD = "id";
    String TYPE_FIELD = "type";
    String NAME_FIELD = "name";
    String CREATE_DATE_FIELD = "create_date";
    String MODIFY_DATE_FIELD = "modify_date";
    String IS_DELETE_FIELD = "is_delete";

    int ID_INDEX = 1001;
    int TYPE_INDEX = 1002;
    int NAME_INDEX = 1003;
    int CREATE_DATE_INDEX = 1004;
    int MODIFY_DATE_INDEX = 1005;
    int IS_DELETE_INDEX = 1006;

    String OBJECT_ID_INCREASE_NAME = "SP_OBJECT_ID_INCREASE";

    Long getId();

    String getType();

    String getName();

    Date getCreateDate();

    Date getModifyDate();

    Boolean getDelete();

    /**
     * 是否为新建对象
     */
    boolean isNewObject();

    /**
     * 设置对象名称
     */
    void setName(String name);

    /**
     * 将对象持久化
     */
    void store();

    /**
     * 将对象从持久化删除
     */
    void remove();

    /**
     * 生成数据库存储实体对象
     */
    SpDataObjectDO generateDataObjectDO();

    /**
     * 获取高级搜索存储数据
     */
    Map<String, Object> getDocument();

    /**
     * 保存对象前的操作
     */
    void beforeStoreOperations();

    /**
     * 保存对象后的操作
     */
    void afterStoreOperations();

}
