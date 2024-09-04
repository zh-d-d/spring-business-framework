package com.dogbody.spring.framework.sequence.core;

import com.dogbody.spring.framework.sequence.SequenceDefinition;
import com.dogbody.spring.framework.sequence.cache.CacheProvider;
import com.dogbody.spring.framework.sequence.constant.CacheMode;
import com.dogbody.spring.framework.sequence.dao.DataAccessor;
import com.dogbody.spring.framework.sequence.exception.SequenceException;

import java.util.List;
import java.util.Optional;

/**
 * @author zhangdd on 2024/8/6
 */
public class Sequence {
    private final SequenceDefinition definition;
    private final CacheProvider cacheProvider;
    private final DataAccessor dataAccessor;

    public Sequence(SequenceDefinition definition, List<CacheProvider> cacheProviders, DataAccessor dataAccessor) {
        this.definition = definition;
        this.dataAccessor = dataAccessor;
        this.cacheProvider = cacheProviders.stream()
                .filter(item -> item.support(CacheMode.valueOf(definition.getCacheMode())))
                .findFirst()
                .orElseThrow(() -> {
                    String msg = String.format("no cache provider support cache mode: %s", definition.getCacheMode());
                    return new IllegalArgumentException(msg);
                });
    }


    private void reloadCache() {
        long newNumber = dataAccessor.grow(definition);
        for (int i = 0; i < definition.getCacheSize(); i++) {
            final long number = newNumber - (long) (definition.getCacheSize() - i) * definition.getStep();
            cacheProvider.add(definition.getKey(), number);
        }
    }

    public Long next() {
        try {
            return doGetNext();
        } catch (Exception e) {
            String msg = String.format("fail to get next of sequence %s", definition.toString());
            throw new SequenceException(msg, e);
        }
    }

    private synchronized Long doGetNext() {
        Optional<Long> number = cacheProvider.poll(definition.getKey());
        if (number.isPresent()) {
            return number.get();
        } else {
            reloadCache();
            number = cacheProvider.poll(definition.getKey());
            if (number.isEmpty()) {
                throw new SequenceException("序列值获取失败");
            }
            return number.get();
        }
    }
}
