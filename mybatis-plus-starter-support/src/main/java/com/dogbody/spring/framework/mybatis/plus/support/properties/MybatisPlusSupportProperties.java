package com.dogbody.spring.framework.mybatis.plus.support.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangdd on 2024/3/29
 */
@Data
@ConfigurationProperties(prefix = "dogbody.mybatis")
public class MybatisPlusSupportProperties {

    /**
     * 自动设置创建时间和更新时间值
     */
    private boolean autoSetTime = true;
}
