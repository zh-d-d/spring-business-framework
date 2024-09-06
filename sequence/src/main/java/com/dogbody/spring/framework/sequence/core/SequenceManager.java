package com.dogbody.spring.framework.sequence.core;

import com.dogbody.spring.framework.sequence.SequenceDefinition;
import com.dogbody.spring.framework.sequence.SequenceGenerator;
import com.dogbody.spring.framework.sequence.cache.CacheProvider;
import com.dogbody.spring.framework.sequence.dao.DataAccessor;
import com.dogbody.spring.framework.sequence.exception.SequenceException;

import java.util.*;

/**
 * @author zhangdd on 2024/8/6
 */
public class SequenceManager implements SequenceGenerator {

    /**
     * 访问数据库里序列配置信息
     */
    private final DataAccessor dataAccessor;
    /**
     * 序列获取 提供者
     */
    private final List<CacheProvider> cacheProviders;

    private final Map<String, Sequence> sequencesMap = new HashMap<>();

    public SequenceManager(DataAccessor dataAccessor, List<CacheProvider> cacheProviders) {
        this.dataAccessor = dataAccessor;
        this.cacheProviders = cacheProviders;
        dataAccessor.checkTable();
    }

    @Override
    public Long nextVal(String key) {
        if (sequencesMap.containsKey(key)) {
            return sequencesMap.get(key).next();
        } else {
            this.load(key);
            return this.nextVal(key);
        }
    }


    /**
     * 1 从数据库加载已存在的序列配置信息
     * 2 如果序列配置不存在则抛出异常
     * 3 将查找的配置信息加载到map中进行管理
     */
    public void load(String key) {
        final Optional<SequenceDefinition> definitionFromDB = dataAccessor.find(key);
        SequenceDefinition definition = definitionFromDB.orElseThrow(() -> new SequenceException(String.format("不存在的key[%s]", key)));
        Sequence sequence = new Sequence(definition, cacheProviders, dataAccessor);
        this.sequencesMap.put(definition.getKey(), sequence);
    }
}
