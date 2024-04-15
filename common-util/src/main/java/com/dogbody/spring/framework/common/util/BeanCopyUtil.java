package com.dogbody.spring.framework.common.util;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.cglib.CglibUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhangdd on 2023/4/13
 */
public class BeanCopyUtil {

    /**
     * copy object list
     */
    public static <Target> List<Target> copyList(List<?> sourceList, Class<Target> targetType) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>(0);
        }
        return sourceList.stream()
                .map(source -> copy(source, targetType))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * copy object
     */
    public static <Target> Target copy(Object source, Class<Target> targetType) {
        if (source == null || targetType == null) {
            return null;
        }
        Target target = ReflectUtil.newInstanceIfPossible(targetType);
        return copy(source, target);
    }

    /**
     * copy object
     */
    public static <Target> Target copy(Object source, Target target) {
        if (source == null || target == null) {
            return null;
        }
        CglibUtil.copy(source, target);
        return target;
    }
}