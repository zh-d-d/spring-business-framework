package com.dogbody.spring.framework.webhook.wechat.constant;

import lombok.Getter;

/**
 * @author zhangdd on 2024/4/19
 */
@Getter
public enum MessageEnum {
    text("text", "文本"),
    markdown("markdown", "markdown"),

    template_card("template_card", "模版卡片");

    private final String type;
    private final String desc;

    MessageEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
