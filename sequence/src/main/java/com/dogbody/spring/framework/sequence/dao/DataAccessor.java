package com.dogbody.spring.framework.sequence.dao;

import com.dogbody.spring.framework.sequence.SequenceDefinition;

import java.util.Optional;

/**
 * @author zhangdd on 2024/8/6
 */
public interface DataAccessor {

    void init();

    Optional<SequenceDefinition> find(String key);

    void insert(SequenceDefinition definition);

    void update(SequenceDefinition definition);

    long grow(SequenceDefinition definition);

}
