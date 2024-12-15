package cn.wanfeng.sp.api.reposiotry;

import cn.wanfeng.sp.api.domain.SpBaseObject;

import javax.validation.constraints.NotNull;

/**
 * @date: 2024-12-15 15:03
 * @author: luozh.wanfeng
 * @description: 领域对象操作接口，DomainClass为继承自基础对象类SpBaseObject的子类
 * @since: 1.0
 */
public interface DomainRepository<DomainClass extends SpBaseObject> {

    /**
     * 根据id获取领域对象
     * @param id id
     * @return 领域对象
     */
    DomainClass find(@NotNull Long id);

    /**
     * 保存领域对象到数据库
     * @param object 领域对象
     */
    void store(@NotNull DomainClass object);

    /**
     * 在数据库中删除该领域对象
     * @param object 领域对象
     */
    void remove(@NotNull DomainClass object);
}
