package com.dogbody.spring.framework.sequence.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangdd on 2024/8/6
 */
@Data
@ConfigurationProperties(prefix = "sequence.config")
public class SequenceProperties {
    /**
     * 默认表名
     */
    private final String DEFAULT_TABLE_NAME = "seq_sequence";

    /**
     * 用于存储 序列号信息的表名
     */
    private String tableName = DEFAULT_TABLE_NAME;

    /**
     * redis 缓存时间 单位是秒
     * 默认时间7天 86400 秒
     */
    private long redisCacheExpire = TimeUnit.DAYS.toSeconds(7);


}
