package cn.wanfeng.sp.storage.search;

import cn.wanfeng.sp.api.model.SpPropertyValue;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.util.LogUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SupportSearchStorage: desc.
 *
 * @date: 2026-02-05 16:01
 * @author: wanfeng·Aura
 */
public interface SupportSearchStorage {

    Logger log = LogUtil.getSimpleProtoLogger();

    /**
     * 转换为能直接保存ES的对象
     * @return Map
     */
    default Map<String, SpPropertyValue> asDocument(){
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            Map<String, SpPropertyValue> document = new LinkedHashMap<>(fields.length);
            for (Field field : fields) {
                String propertyName;
                if(field.isAnnotationPresent(TableField.class)){
                    propertyName = field.getAnnotation(TableField.class).value();
                }else if(field.isAnnotationPresent(TableId.class)){
                    propertyName = field.getAnnotation(TableId.class).value();
                } else{
                    log.warn("存在未声明存储字段的Field({})，Class({})", field.getName(), this.getClass().getName());
                    continue;
                }

                if(document.containsKey(propertyName)){
                    log.error("存储字段重复 propertyName({}) Field({})，Class({})", propertyName, field.getName(), this.getClass().getName());
                }

                field.setAccessible(true);
                SpPropertyValue spPropertyValue = SpPropertyValue.build(field.getType(), field.get(this));
                document.put(propertyName, spPropertyValue);
            }
            return document;
        } catch (IllegalAccessException e) {
            throw new SpException(e, "asDocument 转换SearchStorage存储数据反射权限异常");
        }
    }
}
