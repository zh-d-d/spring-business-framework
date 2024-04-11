package com.dogbody.spring.framework.sms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangdd on 2024/4/7
 */
@Data
@ConfigurationProperties("sms.aliyun")
public class SMSProperties {
    private String accessKey;
    private String secret;
    private String endPoint;
}
