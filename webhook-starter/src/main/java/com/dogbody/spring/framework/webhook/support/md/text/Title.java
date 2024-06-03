package com.dogbody.spring.framework.webhook.support.md.text;

import com.dogbody.spring.framework.webhook.support.md.MarkdownElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangdd on 2024/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Title extends MarkdownElement {
    private TitleEnum titleEnum;

    public Title(Object value, TitleEnum titleEnum) {
        this.value = value;
        this.titleEnum = titleEnum;
    }

    @Override
    protected String getValue() {
        if (null == value) {
            return "";
        }
        String titleLevel = "";
        switch (titleEnum) {
            case H1:
                titleLevel = "# ";
                break;
            case H2:
                titleLevel = "## ";
                break;
            case H3:
                titleLevel = "### ";
                break;
            case H4:
                titleLevel = "#### ";
                break;
            case H5:
                titleLevel = "##### ";
                break;
            case H6:
                titleLevel = "###### ";
                break;
        }
        return titleLevel + value;
    }
}
