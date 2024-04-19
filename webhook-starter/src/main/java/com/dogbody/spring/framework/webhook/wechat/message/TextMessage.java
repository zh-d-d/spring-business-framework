package com.dogbody.spring.framework.webhook.wechat.message;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dogbody.spring.framework.webhook.wechat.Message;
import com.dogbody.spring.framework.webhook.wechat.constant.MessageEnum;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author zhangdd on 2024/4/19
 */
@Data
@Builder
public class TextMessage implements Message {

    @Override
    public String messageType() {
        return MessageEnum.text.getType();
    }

    @Override
    public String messageField() {
        return MessageEnum.text.getType();
    }


    /**
     * 文本消息内容
     * 最长不超过2048个字节，必须是utf8编码
     */
    private String content;

    /**
     * userid的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人，如果开发者获取不到userid，可以使用mentioned_mobile_list
     */
    private List<String> mentionedList;
    /**
     * 手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人
     */
    private List<String> mentionedMobileList;


    @Override
    public String value() {
        JSONObject root = JSONUtil.createObj();
        root.set("msgtype", messageType());

        JSONObject message = JSONUtil.createObj();
        message.set("content", content);
        if (CollectionUtils.isNotEmpty(mentionedList)) {
            JSONArray array = JSONUtil.createArray();
            array.addAll(mentionedList);
            message.set("mentioned_list", array);
        }
        if (CollectionUtils.isNotEmpty(mentionedMobileList)) {
            JSONArray array = JSONUtil.createArray();
            array.addAll(mentionedMobileList);
            message.set("mentioned_mobile_list", array);
        }
        root.set(messageField(), message);

        return root.toString();
    }
}
