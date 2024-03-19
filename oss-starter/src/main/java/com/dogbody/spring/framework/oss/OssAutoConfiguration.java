package com.dogbody.spring.framework.oss;

import com.dogbody.spring.framework.oss.properties.OssProperties;
import com.dogbody.spring.framework.oss.util.OssUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zhangdd on 2024/3/19
 */
@AutoConfiguration
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

    public OssAutoConfiguration(ConfigurableApplicationContext applicationContext,OssProperties ossProperties) {
        OssUtil.initialize(ossProperties);
    }

}
