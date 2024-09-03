package com.dogbody.spring.framework.sequence.cache;

import com.dogbody.spring.framework.sequence.constant.CacheMode;
import com.dogbody.spring.framework.sequence.properties.SequenceProperties;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangdd on 2024/8/6
 */
public class RedisCacheProvider implements CacheProvider {
    private final static String KEY_PREFIX = "sequence:key:";

    private final RedisTemplate<String, Object> redisTemplate;

    private final SequenceProperties sequenceProperties;

    public RedisCacheProvider(RedisTemplate<String, Object> redisTemplate, SequenceProperties sequenceProperties) {
        this.redisTemplate = redisTemplate;
        this.sequenceProperties = sequenceProperties;
    }

    @Override
    public void add(String key, Long number) {
        redisTemplate.opsForSet().add(KEY_PREFIX + key, number);
        redisTemplate.expire(KEY_PREFIX + key, sequenceProperties.getRedisCacheExpire(), TimeUnit.SECONDS);
    }

    @Override
    public Optional<Long> poll(String key) {
        Object value = redisTemplate.opsForSet().pop(KEY_PREFIX + key);
        return Objects.isNull(value) ? Optional.empty() : Optional.of(Long.valueOf(value.toString()));
    }

    @Override
    public boolean support(CacheMode cacheMode) {
        return CacheMode.REDIS.equals(cacheMode);
    }
}
