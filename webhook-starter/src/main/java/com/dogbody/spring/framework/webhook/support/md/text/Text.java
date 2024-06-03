package com.dogbody.spring.framework.webhook.support.md.text;

import com.dogbody.spring.framework.webhook.support.md.MarkdownElement;
import com.dogbody.spring.framework.webhook.support.md.style.Style;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangdd on 2024/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Text extends MarkdownElement {


    public Text(Object value) {
        this(value, null);
    }

    public Text(Object value, Style style) {
        this.value = value;
        this.style = style;
    }

    private String getStyleContent() {
        if (null == style) {
            return "";
        }
        if (null == style.getColor()) {
            return null;
        }
        return "color=\"" + style.getColor().getValue() + "\"";
    }

    public String getValue() {

        return "<font " + getStyleContent() + ">" + value.toString() + "</font>";
    }
}
