package com.cloudprint.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 集合工具类
 */
public class CollectionUtils {
    /**
     * 转换集合
     * @param source 源集合
     * @param converter 转换函数
     * @param predicate 过滤条件
     * @param <T> 源类型
     * @param <R> 目标类型
     * @return 转换后的集合
     */
    public static <T, R> List<R> convertList(List<T> source, Function<T, R> converter, Predicate<T> predicate) {
        if (source == null || source.isEmpty()) {
            return new ArrayList<>();
        }
        return source.stream()
                .filter(predicate)
                .map(converter)
                .collect(Collectors.toList());
    }
    
    /**
     * 查找第一个符合条件的元素
     * @param source 源集合
     * @param predicate 条件
     * @param <T> 元素类型
     * @return 符合条件的第一个元素，否则返回null
     */
    public static <T> T findFirst(List<T> source, Predicate<T> predicate) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return source.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 转换集合
     * @param source 源集合
     * @param converter 转换函数
     * @param predicate 过滤条件
     * @param <T> 源类型
     * @param <R> 目标类型
     * @return 转换后的集合
     */
    public static <T, R> List<R> convertList(Iterable<T> source, Function<T, R> converter, Predicate<T> predicate) {
        List<R> result = new ArrayList<>();
        if (source == null) {
            return result;
        }
        for (T item : source) {
            if (predicate.test(item)) {
                result.add(converter.apply(item));
            }
        }
        return result;
    }
    
    /**
     * 查找第一个符合条件的元素
     * @param source 源集合
     * @param predicate 条件
     * @param <T> 元素类型
     * @return 符合条件的第一个元素，否则返回null
     */
    public static <T> T findFirst(Iterable<T> source, Predicate<T> predicate) {
        if (source == null) {
            return null;
        }
        for (T item : source) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }
}