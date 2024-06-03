package com.dogbody.spring.framework.webhook.support.md.text;

import com.dogbody.spring.framework.webhook.support.md.MarkdownElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangdd on 2024/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Quote extends MarkdownElement {

    public Quote(Object value) {
        this.value = value;
    }

    @Override
    protected String getValue() {
        return "> " + value.toString();
    }
}
