package com.dogbody.spring.framework.mybatis.plus.support;

import com.dogbody.spring.framework.mybatis.plus.support.injector.MetaObjectInjector;
import com.dogbody.spring.framework.mybatis.plus.support.properties.MybatisPlusSupportProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@EnableConfigurationProperties(MybatisPlusSupportProperties.class)
public class MybatisPlusSupportAutoConfiguration {


    @Bean
    public MetaObjectInjector metaObjectInjector(MybatisPlusSupportProperties mybatisPlusSupportProperties) {
        return new MetaObjectInjector(mybatisPlusSupportProperties);
    }

}
