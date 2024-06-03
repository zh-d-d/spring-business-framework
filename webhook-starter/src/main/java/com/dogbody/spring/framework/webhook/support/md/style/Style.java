package com.dogbody.spring.framework.webhook.support.md.style;

import lombok.Data;
import lombok.Getter;

/**
 * @author zhangdd on 2024/6/3
 */
@Data
public class Style {

    private Color color;

    public Style(Color color) {
        this.color = color;
    }

    @Getter
    public enum Color {
        info("info", "绿色"),
        comment("comment", "灰色"),
        warning("warning", "橙红色"),
        ;
        private final String value;
        private final String desc;

        Color(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }
}
