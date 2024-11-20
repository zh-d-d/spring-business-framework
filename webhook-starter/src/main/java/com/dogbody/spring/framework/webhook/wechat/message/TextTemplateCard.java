package com.dogbody.spring.framework.webhook.wechat.message;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dogbody.spring.framework.webhook.wechat.Message;
import com.dogbody.spring.framework.webhook.wechat.constant.MessageEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * @author zhangdd on 2024/11/20
 *
 * <a href="https://developer.work.weixin.qq.com/document/path/99110#%E6%96%87%E6%9C%AC%E9%80%9A%E7%9F%A5%E6%A8%A1%E7%89%88%E5%8D%A1%E7%89%87">...</a>
 */
@NoArgsConstructor
@Data
public class TextTemplateCard implements Message {

    private MainTitle mainTitle;
    private EmphasisContent emphasisContent;
    private QuoteArea quoteArea;

    private CardAction cardAction;

    @Override
    public String messageType() {
        return MessageEnum.template_card.getType();
    }

    @Override
    public String value() {
        JSONObject root = JSONUtil.createObj();
        root.set("msgtype", messageType());
        JSONObject templateCard = JSONUtil.createObj();
        templateCard.set("card_type", "text_notice");
        templateCard.set("main_title", Optional.ofNullable(mainTitle).map(MainTitle::value).orElse(null));
        templateCard.set("emphasis_content", Optional.ofNullable(emphasisContent).map(EmphasisContent::value).orElse(null));
        templateCard.set("quote_area", Optional.ofNullable(quoteArea).map(QuoteArea::value).orElse(null));
        templateCard.set("card_action", Optional.ofNullable(cardAction).map(CardAction::value).orElse(null));
        root.set("template_card", templateCard);
        return root.toString();
    }


    @Data
    public static class MainTitle {
        private String title;
        private String desc;

        private JSONObject value() {
            JSONObject root = JSONUtil.createObj();
            root.set("title", title);
            root.set("desc", desc);
            return root;
        }
    }

    @Data
    public static class EmphasisContent {
        private String title;
        private String desc;

        private JSONObject value() {
            JSONObject root = JSONUtil.createObj();
            root.set("title", title);
            root.set("desc", desc);
            return root;
        }
    }


    @Data
    public static class QuoteArea {
        private Integer type;
        private String url;
        private String appid;
        private String pagePath;
        private String title;
        private String quoteText;

        private JSONObject value() {
            JSONObject root = JSONUtil.createObj();
            root.set("type", type);
            root.set("url", url);
            root.set("appid", appid);
            root.set("pagepath", pagePath);
            root.set("title", title);
            root.set("quote_text", quoteText);
            return root;
        }
    }

    @Data
    public static class CardAction{
        private Integer type;
        private String url;
        private String appid;
        private String pagePath;

        private JSONObject value() {
            JSONObject root = JSONUtil.createObj();
            root.set("type",type);
            root.set("url",url);
            root.set("appid",appid);
            root.set("pagepath",pagePath);
            return root;
        }
    }
}
