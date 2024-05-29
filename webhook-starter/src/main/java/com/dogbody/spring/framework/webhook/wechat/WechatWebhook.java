package com.dogbody.spring.framework.webhook.wechat;

import cn.hutool.http.HttpUtil;

/**
 * @author zhangdd on 2024/4/19
 */
public class WechatWebhook {

    public void send(String webhookUrl, Message message) {
        String value = message.value();
        HttpUtil.post(webhookUrl, value);
    }
}
