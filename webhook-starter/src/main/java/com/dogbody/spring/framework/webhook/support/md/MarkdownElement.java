package com.dogbody.spring.framework.webhook.support.md;

import com.dogbody.spring.framework.webhook.support.md.style.Style;

/**
 * @author zhangdd on 2024/6/3
 */
public abstract class MarkdownElement {

    protected Object value;

    protected Style style;

    protected abstract String getValue();


}
