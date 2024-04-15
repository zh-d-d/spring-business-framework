package com.dogbody.spring.framework.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;

/**
 * @author liaozan
 * @since 2022/8/21
 */
@Slf4j
public class StreamUtils {

    private static final Set<Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));

    public static <T, E> List<E> toList(Collection<T> data, Function<T, E> mapper) {
        return toList(data, mapper, false);
    }

    public static <T, E> List<E> toList(Collection<T> data, Function<T, E> mapper, boolean distinct) {
        return toList(data, mapper, distinct, false);
    }

    public static <T, E> List<E> toList(Collection<T> data, Function<T, E> mapper, boolean distinct, boolean ignoreNull) {
        return extract(data, mapper, distinct, ignoreNull, Collectors.toList());
    }

    public static <T, E> Set<E> toSet(Collection<T> data, Function<T, E> mapper) {
        return toSet(data, mapper, false);
    }

    public static <T, E> Set<E> toSet(Collection<T> data, Function<T, E> mapper, boolean ignoreNull) {
        return extract(data, mapper, ignoreNull, false, Collectors.toSet());
    }

    public static <K, T> Map<K, T> toMap(Collection<T> data, Function<T, K> keyMapper) {
        return toMap(data, keyMapper, false);
    }

    public static <K, T> Map<K, T> toMap(Collection<T> data, Function<T, K> keyMapper, boolean ordered) {
        return toMap(data, keyMapper, Function.identity(), ordered);
    }

    public static <K, T, V> Map<K, V> toMap(Collection<T> data, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return toMap(data, keyMapper, valueMapper, false);
    }

    public static <K, T, V> Map<K, V> toMap(Collection<T> data, Function<T, K> keyMapper, Function<T, V> valueMapper, boolean ordered) {
        Supplier<Map<K, V>> mapFactory = HashMap::new;
        if (ordered) {
            mapFactory = LinkedHashMap::new;
        }
        return toMap(data, keyMapper, valueMapper, mapFactory);
    }

    public static <K, T, V, M extends Map<K, V>> Map<K, V> toMap(Collection<T> data, Function<T, K> keyMapper, Function<T, V> valueMapper, Supplier<M> mapFactory) {
        CollectorImpl<T, M, M> collector = createCollector(mapFactory, createAccumulator(keyMapper, valueMapper));
        return Optional.ofNullable(data)
                .orElse(emptyList())
                .stream()
                .collect(collector);
    }

    public static <K, T> Map<K, List<T>> groupBy(Collection<T> data, Function<T, K> mapper) {
        return groupBy(data, mapper, false);
    }

    public static <K, T> Map<K, List<T>> groupBy(Collection<T> data, Function<T, K> keyMapper, boolean ignoreNullKey) {
        return groupBy(data, keyMapper, Collectors.toList(), ignoreNullKey);
    }

    public static <K, T, V> Map<K, V> groupBy(Collection<T> data, Function<T, K> mapper, Collector<T, ?, V> collectors) {
        return groupBy(data, mapper, collectors, false);
    }

    public static <K, T, V> Map<K, V> groupBy(Collection<T> data, Function<T, K> mapper, Collector<T, ?, V> collectors, boolean ignoreNullKey) {
        return groupBy(data, mapper, Function.identity(), collectors, ignoreNullKey);
    }

    public static <K, T, V> Map<K, List<V>> groupBy(Collection<T> data, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return groupBy(data, keyMapper, valueMapper, Collectors.toList(), false);
    }

    public static <K, T, V> Map<K, List<V>> groupBy(Collection<T> data, Function<T, K> keyMapper, Function<T, V> valueMapper, boolean ignoreNullKey) {
        return groupBy(data, keyMapper, valueMapper, Collectors.toList(), ignoreNullKey);
    }

    public static <K, T, V, C> Map<K, C> groupBy(Collection<T> data, Function<T, K> keyMapper, Function<T, V> valueMapper, Collector<V, ?, C> collector) {
        return groupBy(data, keyMapper, valueMapper, collector, false);
    }

    public static <K, T, V, C> Map<K, C> groupBy(Collection<T> data, Function<T, K> keyMapper, Function<T, V> valueMapper, Collector<V, ?, C> collector, boolean ignoreNullKey) {
        return groupBy(data, keyMapper, valueMapper, collector, ignoreNullKey, HashMap::new);
    }

    public static <K, T, V, C> Map<K, C> groupBy(Collection<T> data, Function<T, K> keyMapper, Function<T, V> valueMapper, Collector<V, ?, C> collector, boolean ignoreNullKey, Supplier<Map<K, C>> mapSupplier) {
        Stream<T> stream = Optional.ofNullable(data)
                .orElse(emptyList())
                .stream();
        if (ignoreNullKey) {
            stream = stream.filter(item -> null != keyMapper.apply(item));
        }
        return stream.collect(groupingBy(keyMapper, mapSupplier, mapping(valueMapper, collector)));
    }

    public static <T> String join(Collection<T> data, CharSequence delimiter) {
        return join(data, delimiter, Objects::toString);
    }

    public static <T> String join(Collection<T> data, CharSequence delimiter, Function<T, ? extends CharSequence> toStringFunction) {
        return join(data, delimiter, "", "", toStringFunction);
    }

    public static <T> String join(Collection<T> data, CharSequence delimiter, String prefix, String suffix, Function<T, ? extends CharSequence> toStringFunction) {
        return Optional.ofNullable(data)
                .orElse(emptyList())
                .stream().map(toStringFunction).collect(joining(delimiter, prefix, suffix));
    }

    public static <T, E, R> R extract(Collection<T> data, Function<T, E> mapper, boolean distinct, boolean ignoreNull, Collector<E, ?, R> collector) {
        Predicate<E> predicate = null;
        if (ignoreNull) {
            predicate = Objects::nonNull;
        }
        return extract(data, mapper, predicate, distinct, collector);
    }

    public static <T, E, R> R extract(Collection<T> data, Function<T, E> mapper, Predicate<E> predicate, boolean distinct, Collector<E, ?, R> collector) {
        Stream<E> stream = Optional.ofNullable(data)
                .orElse(emptyList())
                .stream()
                .map(mapper);
        if (distinct) {
            stream = stream.distinct();
        }
        if (predicate != null) {
            stream = stream.filter(predicate);
        }
        return stream.collect(collector);
    }

    /*
     * 以下代码是为了实现重复 Map key 的时候打印自定义消息
     * 实测 toMap 时并不会调用到 combiner 的方法, 错误信息是通过 accumulator 抛出,所以只能通过定制 accumulator 实现
     * 但是 CollectorImpl 是私有内部类, 所以一并重写
     */
    private static <K, T, V, M extends Map<K, V>> CollectorImpl<T, M, M> createCollector(Supplier<M> mapFactory, BiConsumer<M, T> accumulator) {
        return new CollectorImpl<>(mapFactory, accumulator, mergeFunction(null), castingIdentity(), CH_ID);
    }

    private static <K, T, V, M extends Map<K, V>> BiConsumer<M, T> createAccumulator(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return (map, element) -> {
            K key = keyMapper.apply(element);
            V value = valueMapper.apply(element);
            map.merge(key, value, mergeFunction(key));
        };
    }

    private static <K, V> BinaryOperator<V> mergeFunction(K key) {
        return (oldValue, newValue) -> {
            try {
                String oldValueJson = JacksonUtils.toPrettyJsonString(oldValue);
                String newValueJson = JacksonUtils.toPrettyJsonString(newValue);
                logDuplicateMessage(key, oldValueJson, newValueJson);
            } catch (Exception ignore) {
                logDuplicateMessage(key, oldValue, newValue);
            }
            return oldValue;
        };
    }

    private static <K> void logDuplicateMessage(K key, Object oldValue, Object newValue) {
        if (key != null) {
            log.warn("The same key was encountered when toMap,\nkey: {}, \noldValue: {}, \nnewValue: {},\nreturn the oldValue", key, oldValue, newValue);
        } else {
            log.warn("The same key was encountered when toMap, \noldValue: {}, \nnewValue: {},\nreturn the oldValue", oldValue, newValue);
        }
    }

    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    private static class CollectorImpl<T, A, R> implements Collector<T, A, R> {

        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        private CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A, R> finisher, Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }

    }

}