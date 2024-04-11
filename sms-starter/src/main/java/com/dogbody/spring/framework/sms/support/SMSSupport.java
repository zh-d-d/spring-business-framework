package com.dogbody.spring.framework.sms.support;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.dogbody.spring.framework.sms.properties.SMSProperties;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author zhangdd on 2024/4/7
 */
@Slf4j
public class SMSSupport {

    @Resource
    private SMSProperties smsProperties;

    private IAcsClient client;

    @PostConstruct
    public void init() {
        // 初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsProperties.getAccessKey(), smsProperties.getSecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "Dysmsapi", smsProperties.getEndPoint());
        client = new DefaultAcsClient(profile);
    }

    private CommonRequest buildRequest() {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        //短信API产品域名（接口地址固定，无需修改）
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        return request;
    }

//    private void sendMessage(CommonRequest request) {
//        log.debug("sendSmsMsg request {}", request);
//
//        CommonResponse commonResponse;
//        try {
//            commonResponse = client.getCommonResponse(request);
//        } catch (ClientException e) {
//
//            throw new SMSException(SYSTEM_MAINTENANCE, e.getMessage());
//        }
//        if (commonResponse != null) {
//            JsonNode jsonNode = JacksonUtils.getJsonNode(commonResponse.getData());
//            // 流控错误代码
//            String code = jsonNode.get("Code").asText();
//            if ("OK".equals(code)) {
//                return;
//            }
//            if ("isv.BUSINESS_LIMIT_CONTROL".equals(code)) {
//                throw new SMSException(SYSTEM_MAINTENANCE, "获取验证码太频繁，请稍后再试");
//            } else if ("isv.MOBILE_NUMBER_ILLEGAL".equals(code)) {
//                // PhoneNumbers参数请传入11位国内号段的手机号码
//                throw new SMSException(SYSTEM_MAINTENANCE, "请传入11位国内号段的手机号码");
//            } else {
//                throw new SMSException(SYSTEM_MAINTENANCE, jsonNode.get("Message").asText());
//            }
//        }
//    }

}
