package com.dogbody.spring.framework.webhook.support.md.text;

import com.dogbody.spring.framework.webhook.support.md.MarkdownElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangdd on 2024/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BoldText extends MarkdownElement {

    public BoldText(Object value) {
        this.value = value;
    }


    @Override
    protected String getValue() {
        if (null == value) {
            return "";
        }
        return "**" + value + "**";
    }
}
