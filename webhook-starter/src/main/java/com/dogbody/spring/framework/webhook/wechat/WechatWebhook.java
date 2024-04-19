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

//    public static void main(String[] args) {
//        WechatWebhook webhook = new WechatWebhook();
////        TextMessage message = TextMessage.builder().content("文本测试")
////                .mentionedMobileList(List.of("1****0"))
////                .build();
//
//        MarkdownMessage message = MarkdownMessage.builder()
//                .content("实时新增用户反馈<font color=\"warning\">132例</font>")
//                .build();
//
//
//        String webhookUrl = "";
//        webhook.send(webhookUrl, message);
//    }
}
