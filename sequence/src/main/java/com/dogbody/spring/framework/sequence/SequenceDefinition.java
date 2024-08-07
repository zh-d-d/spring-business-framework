package com.dogbody.spring.framework.sequence;

import com.dogbody.spring.framework.sequence.constant.CacheMode;
import lombok.Data;

import java.util.Objects;

/**
 * Sequence 的定义信息
 * 每个业务Sequence 可以根据实际情况配置步长、初始值、缓存模式等信息
 *
 * @author zhangdd on 2024/8/6
 */
@Data
public class SequenceDefinition {
    /**
     * 业务信息名称
     */
    private String name;
    /**
     * 业务信息标识，用于标识该序列号的使用场景
     */
    private String key;
    /**
     * 初始值
     */
    private long initialValue = 1L;
    /**
     * 步长
     * 不允许为0
     * 为正数表示递增
     * 为负数表示递减
     */
    private int step = 1;
    private int cacheSize = 100;
    private String cacheMode = CacheMode.MEMORY.name();

    public SequenceDefinition(String key) {
        if (Objects.isNull(key) || key.length() == 0 || key.length() > 255) {
            throw new IllegalArgumentException("length of sequence key minimum value is 1 and maximum value is 255.");
        }
        this.key = key;
        this.name=key;
    }

    public SequenceDefinition(String name, String key, long initialValue, int step, int cacheSize, String cacheMode) {
        if (Objects.isNull(key) || key.length() == 0 || key.length() > 255) {
            throw new IllegalArgumentException("length of sequence key minimum value is 1 and maximum value is 255.");
        }
        if (0 == step) {
            throw new IllegalArgumentException("sequence step should not be zero.");
        }
        this.name = name;
        this.key = key;
        this.initialValue = initialValue;
        this.step = step;
        this.cacheSize = cacheSize;
        CacheMode cacheModeInstance = CacheMode.valueOf(cacheMode);
        this.cacheMode = cacheModeInstance.name();
    }

    public void setKey(String key) {
        if (Objects.isNull(key) || key.length() == 0 || key.length() > 255) {
            throw new IllegalArgumentException("length of sequence key minimum value is 1 and maximum value is 255.");
        } else {
            this.key = key;
        }
    }

    public void setStep(int step) {
        if (0 == step) {
            throw new IllegalArgumentException("sequence step should not be zero.");
        } else {
            this.step = step;
        }
    }

}
