package com.dogbody.spring.framework.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author liaozan
 * @since 2021/12/3
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties {

    private String stsUrl="/api/oss-sts";

    private String accessKeyId;

    private String secretAccessKey;

    private String endpoint;

    private String bucketName;

    private String domain;

    @NestedConfigurationProperty
    private StsProperties sts;

    public boolean isInValid() {
        return accessKeyId == null || secretAccessKey == null;
    }


    @Data
    public static class StsProperties {

        private String endpoint;

        private String roleArn;

        private String roleSessionName;

        private Long durationSeconds = 900L;

    }

}