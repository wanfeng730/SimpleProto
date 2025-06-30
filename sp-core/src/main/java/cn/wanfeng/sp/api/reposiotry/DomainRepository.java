package cn.wanfeng.sp.api.reposiotry;

import cn.wanfeng.proto.constants.SpExceptionMessage;
import cn.wanfeng.sp.anno.Type;
import cn.wanfeng.sp.api.domain.SpBaseObject;
import cn.wanfeng.sp.exception.SpException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    /**
     * 断言对象的类型是否正确
     * @param object 领域对象
     */
    default void assertObjectType(@NotNull DomainClass object) {
        Type typeAnnotation = object.getClass().getAnnotation(Type.class);
        if (Objects.isNull(typeAnnotation)) {
            return;
        }
        String defineObjectType = typeAnnotation.value();
        if (!StringUtils.equals(defineObjectType, object.getType())) {
            throw new SpException(SpExceptionMessage.objectTypeNotEqualsDefine(object.getId(), defineObjectType));
        }
    }
}
