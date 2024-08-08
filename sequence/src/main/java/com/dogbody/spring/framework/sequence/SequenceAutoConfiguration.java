package com.dogbody.spring.framework.sequence;

import com.dogbody.spring.framework.sequence.cache.CacheProvider;
import com.dogbody.spring.framework.sequence.cache.MemoryCacheProvider;
import com.dogbody.spring.framework.sequence.cache.RedisCacheProvider;
import com.dogbody.spring.framework.sequence.core.SequenceManager;
import com.dogbody.spring.framework.sequence.dao.DataAccessor;
import com.dogbody.spring.framework.sequence.dao.DataSourceDataAccessor;
import com.dogbody.spring.framework.sequence.properties.SequenceProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author zhangdd on 2024/8/6
 */
@Configuration
@AutoConfigureAfter({DataSourceAutoConfiguration.class, RedisAutoConfiguration.class})
public class SequenceAutoConfiguration {


    @Bean
    public SequenceProperties sequenceProperties() {
        return new SequenceProperties();
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public DataAccessor datasourceDataAccessor(DataSource dataSource, SequenceProperties sequenceProperties) {
        return new DataSourceDataAccessor(dataSource, sequenceProperties);
    }


    @Configuration
    @ConditionalOnClass(RedisTemplate.class)
    public static class SeqRedisCachePoolConfig {

        @Bean("seqCacheRedisTemplate")
        @ConditionalOnMissingBean(name = "seqCacheRedisTemplate")
        public RedisTemplate<String, Object> seqCacheRedisTemplate(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);
            template.setKeySerializer(RedisSerializer.string());
            template.setValueSerializer(RedisSerializer.json());
            return template;
        }

        @Bean
        @DependsOn("seqCacheRedisTemplate")
        public CacheProvider redisCachePool(RedisTemplate<String, Object> seqCacheRedisTemplate, SequenceProperties sequenceProperties) {
            return new RedisCacheProvider(seqCacheRedisTemplate, sequenceProperties);
        }
    }

    @Bean
    public CacheProvider memoryCacheProvider() {
        return new MemoryCacheProvider();
    }

    @Bean
    public SequenceGenerator sequenceGenerator(DataAccessor dataAccessor, List<CacheProvider> cacheProviders) {
        return new SequenceManager(dataAccessor, cacheProviders);
    }
}
