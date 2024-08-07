package com.dogbody.spring.framework.sequence.cache;

import com.dogbody.spring.framework.sequence.constant.CacheMode;

import java.util.*;

/**
 * @author zhangdd on 2024/8/6
 */
public class MemoryCacheProvider implements CacheProvider {

    Map<String, Queue<Long>> pool = new HashMap<>();

    @Override
    public void add(String key, Long number) {
        Queue<Long> cache = pool.get(key);
        if (Objects.isNull(cache)) {
            synchronized (this) {
                cache = pool.get(key);
                if (Objects.isNull(cache)) {
                    cache = new LinkedList<>();
                    pool.put(key, cache);
                }
            }
        }
        cache.add(number);
    }

    @Override
    public Optional<Long> poll(String key) {
        return Optional.ofNullable(pool.get(key).poll());
    }

    @Override
    public boolean support(CacheMode cacheMode) {
        return CacheMode.MEMORY.equals(cacheMode);
    }
}
