package com.dogbody.spring.framework.sequence;

/**
 * @author zhangdd on 2024/8/6
 */
public interface SequenceGenerator {

    Long nextVal(String key);


}
