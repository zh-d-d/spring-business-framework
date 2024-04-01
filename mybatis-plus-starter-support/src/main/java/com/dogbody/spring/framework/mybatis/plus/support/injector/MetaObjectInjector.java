package com.dogbody.spring.framework.mybatis.plus.support.injector;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.dogbody.spring.framework.mybatis.plus.support.constant.MybatisConstants;
import com.dogbody.spring.framework.mybatis.plus.support.entity.BasePo;
import com.dogbody.spring.framework.mybatis.plus.support.properties.MybatisPlusSupportProperties;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author zhangdd on 2024/3/29
 */
public class MetaObjectInjector implements MetaObjectHandler {

    private final MybatisPlusSupportProperties mybatisPlusSupportProperties;

    public MetaObjectInjector(MybatisPlusSupportProperties mybatisPlusSupportProperties) {
        this.mybatisPlusSupportProperties = mybatisPlusSupportProperties;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        if (mybatisPlusSupportProperties.isAutoSetTime() && BasePo.class.isAssignableFrom(metaObject.getOriginalObject().getClass())) {
            LocalDateTime now = LocalDateTime.now();
            setFieldValByName(MybatisConstants.CREATE_TIME, now, metaObject);
            setFieldValByName(MybatisConstants.MODIFY_TIME, now, metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (mybatisPlusSupportProperties.isAutoSetTime() && BasePo.class.isAssignableFrom(metaObject.getOriginalObject().getClass())) {
            setFieldValByName(MybatisConstants.MODIFY_TIME, LocalDateTime.now(), metaObject);
        }
    }
}
