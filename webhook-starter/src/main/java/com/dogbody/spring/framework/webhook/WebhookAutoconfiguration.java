package com.dogbody.spring.framework.webhook;

import com.dogbody.spring.framework.webhook.wechat.WechatWebhook;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author zhangdd on 2024/4/19
 */
public class WebhookAutoconfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WechatWebhook wechatWebhook() {
        return new WechatWebhook();
    }
}
