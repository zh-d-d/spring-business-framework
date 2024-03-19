package com.dogbody.spring.framework.oss.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.dogbody.spring.framework.oss.entity.UploadCredentials;
import com.dogbody.spring.framework.oss.properties.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhangdd on 2024/3/19
 */
@Slf4j
public class OssUtil {

//    private static OSSClient ossClient;
    private static DefaultAcsClient stsAcsClient;
    private static OssProperties ossProperties;
    private static OssProperties.StsProperties stsProperties;

    public static void initialize(OssProperties properties) {
        if (properties == null || properties.isInValid()) {
            log.warn("ossProperties is invalid, OssUtils will not available until reinitialize with the correct configuration");
            return;
        }
        try {
//            ossClient = (OSSClient) initOssClient(properties);
            stsAcsClient = initStsAcsClient(properties);
            ossProperties = properties;
            stsProperties = properties.getSts();
        } catch (Exception e) {
            log.warn("oss initialize fail, OssUtils will not available until reinitialize with the correct configuration", e);
        }
    }

    private static OSS initOssClient(OssProperties ossProperties) {
        return new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getSecretAccessKey());
    }

    private static DefaultAcsClient initStsAcsClient(OssProperties ossProperties) {
        OssProperties.StsProperties stsProperties = ossProperties.getSts();
        DefaultProfile.addEndpoint("", "Sts", stsProperties.getEndpoint());
        IClientProfile profile = DefaultProfile.getProfile("", ossProperties.getAccessKeyId(), ossProperties.getSecretAccessKey());
        return new DefaultAcsClient(profile);
    }

    private static DefaultAcsClient getStsAcsClient() {
        if (stsAcsClient == null) {
            throw new RuntimeException("stsAcsClient is null");
        }
        return stsAcsClient;
    }

    public static UploadCredentials generateUploadToken() {
        return generateUploadToken(null);
    }

    public static UploadCredentials generateUploadToken(String objectKeyPrefix) {
        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setSysMethod(MethodType.POST);
        request.setRoleArn(stsProperties.getRoleArn());
        request.setRoleSessionName(stsProperties.getRoleSessionName());
        request.setDurationSeconds(stsProperties.getDurationSeconds());
        AssumeRoleResponse response;
        try {
            response = getStsAcsClient().getAcsResponse(request);
        } catch (com.aliyuncs.exceptions.ClientException e) {
            throw new RuntimeException(e);
        }
        AssumeRoleResponse.Credentials credentials = response.getCredentials();
        UploadCredentials uploadCredentials = new UploadCredentials(credentials);
        uploadCredentials.setBucket(ossProperties.getBucketName());

        if (StringUtils.isNotBlank(objectKeyPrefix)) {
            if (!objectKeyPrefix.endsWith("/")) {
                objectKeyPrefix = objectKeyPrefix + "/";
            }
            uploadCredentials.setObjectKey(objectKeyPrefix);
        } else {
            uploadCredentials.setObjectKey("/");
        }

        return uploadCredentials;
    }
}
