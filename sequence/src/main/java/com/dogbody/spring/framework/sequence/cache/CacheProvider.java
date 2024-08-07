package com.dogbody.spring.framework.sequence.cache;

import com.dogbody.spring.framework.sequence.constant.CacheMode;

import java.util.Optional;

/**
 * @author zhangdd on 2024/8/6
 */
public interface CacheProvider {
    void add(String key, Long number);

    Optional<Long> poll(String key);

    boolean support(CacheMode cacheMode);
}
