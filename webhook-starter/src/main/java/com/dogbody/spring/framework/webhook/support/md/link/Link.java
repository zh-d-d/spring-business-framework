package com.dogbody.spring.framework.webhook.support.md.link;

import com.dogbody.spring.framework.webhook.support.md.MarkdownElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangdd on 2024/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Link extends MarkdownElement {

    private String url;

    public Link(Object text, String url) {
        this.value = text;
        this.url = url;
    }


    @Override
    protected String getValue() {
        if (null == url) {
            return "";
        }
        return "[" + value + "]" +
                "(" + url + ")";
    }
}
