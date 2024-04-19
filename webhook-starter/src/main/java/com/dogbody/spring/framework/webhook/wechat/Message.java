package com.dogbody.spring.framework.webhook.wechat;

/**
 * @author zhangdd on 2024/4/19
 */
public interface Message {
    /**
     * 消息类型
     */
    default String messageType() {
        return null;
    }

    /**
     * 消息的字段，不同类型的消息，载体字段名称不同
     * 比如：文本消息的是 text，markdown的是markdown
     */
    default String messageField() {
        return null;
    }

    default String content() {
        return null;
    }

    String value();
}
