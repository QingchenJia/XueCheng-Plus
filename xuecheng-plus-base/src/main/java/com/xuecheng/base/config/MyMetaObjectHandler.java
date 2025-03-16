package com.xuecheng.base.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@ConditionalOnClass(MybatisConfiguration.class)
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldIfPresent(metaObject, "createDate", LocalDateTime.now());
        setFieldIfPresent(metaObject, "changeDate", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldIfPresent(metaObject, "changeDate", LocalDateTime.now());
    }

    private void setFieldIfPresent(MetaObject metaObject, String fieldName, Object value) {
        // 检查字段是否存在并进行填充
        if (metaObject.hasSetter(fieldName)) {
            metaObject.setValue(fieldName, value);
        }
    }
}
