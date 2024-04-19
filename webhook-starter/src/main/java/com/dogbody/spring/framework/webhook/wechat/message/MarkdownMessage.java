package com.dogbody.spring.framework.webhook.wechat.message;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dogbody.spring.framework.webhook.wechat.Message;
import com.dogbody.spring.framework.webhook.wechat.constant.MessageEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @author zhangdd on 2024/4/19
 */
@Data
@Builder
public class MarkdownMessage implements Message {
    @Override
    public String messageType() {
        return MessageEnum.markdown.getType();
    }

    @Override
    public String messageField() {
        return MessageEnum.markdown.getType();
    }


    /**
     * markdown内容，最长不超过4096个字节，必须是utf8编码
     */
    private String content;

    @Override
    public String value() {
        JSONObject root = JSONUtil.createObj();
        root.set("msgtype", messageType());

        JSONObject message = JSONUtil.createObj();
        message.set("content", content);

        root.set(messageField(), message);

        return root.toString();
    }
}
