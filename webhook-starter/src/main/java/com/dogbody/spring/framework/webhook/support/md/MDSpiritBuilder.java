package com.dogbody.spring.framework.webhook.support.md;

import com.dogbody.spring.framework.webhook.support.md.common.Common;
import com.dogbody.spring.framework.webhook.support.md.style.Style;
import com.dogbody.spring.framework.webhook.support.md.text.Quote;
import com.dogbody.spring.framework.webhook.support.md.text.Text;
import com.dogbody.spring.framework.webhook.support.md.text.Title;
import com.dogbody.spring.framework.webhook.support.md.text.TitleEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangdd on 2024/6/3
 */
public class MDSpiritBuilder {
    private final List<MarkdownElement> content = new ArrayList<>();


    public MDSpiritBuilder text(Object value) {
        content.add(new Text(value));
        return this;
    }

    public MDSpiritBuilder text(Object value, Style style) {
        content.add(new Text(value, style));
        return this;
    }

    public MDSpiritBuilder title(Object value, TitleEnum titleEnum) {
        content.add(new Title(value, titleEnum));
        return this;
    }

    public MDSpiritBuilder br() {
        content.add(new Common("\n"));
        return this;
    }

    public MDSpiritBuilder quote(Object value) {
        content.add(new Quote(value));
        return this;
    }


    public String build() {
        StringBuilder builder = new StringBuilder();
        for (MarkdownElement element : content) {
            builder.append(element.getValue());
        }
        return builder.toString();
    }
}
