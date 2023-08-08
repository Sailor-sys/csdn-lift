package com.xzl.csdn.support;

import com.github.pagehelper.Page;

import java.util.List;
import java.util.function.Function;

/**
 * @author：lianp
 * @description：
 * @date：10:48 2019/7/22
 */
public class PagePluginHelper {
    private PagePluginHelper() {
        throw new IllegalStateException("This class can not be instance!");
    }

    public static <T, E> Page<T> convertPage(Page<E> page, ObjectConverter<T, E> converter) {
        Page<T> result = new Page<>();
        result.setPageNum(page.getPageNum());
        result.setPageSize(page.getPageSize());
        result.setTotal(page.getTotal());

        for (E i : page.getResult()) {
            result.add(converter.convert(i));
        }

        return result;
    }

    public static <S, T> Page<T> convert(Page<S> page, Function<S, T> converter) {
        Page<T> result = new Page<>();
        result.setPageNum(page.getPageNum());
        result.setPageSize(page.getPageSize());
        result.setTotal(page.getTotal());

        for (S i : page.getResult()) {
            result.add(converter.apply(i));
        }

        return result;
    }

    /**
     * 内存分页
     *
     * @param rows     所有记录
     * @param pageInfo 颁参数
     * @param <E>      记录类型
     * @return 分页后的数据
     */
    public static <E> Page<E> paginateInMemory(List<E> rows, PageInfo pageInfo) {
        return paginateInMemory(rows, pageInfo, a -> a);
    }

    /**
     * 内存分页
     *
     * @param rows     所有记录
     * @param pageInfo 颁参数
     * @param <S>      记录类型
     * @param <T>      返回记录类型
     * @return 分页后的数据
     */
    public static <S, T> Page<T> paginateInMemory(List<S> rows, PageInfo pageInfo, Function<S, T> function) {

        Page<T> page = new Page<>(pageInfo.getPageIndex(), pageInfo.getPageSize());

        int pageSize = pageInfo.getPageSize();
        int start = (pageInfo.getPageIndex() - 1) * pageSize; // 起始记录，从0开始
        int end = start + pageSize; // 结束记录

        end = end > rows.size() ? rows.size() : end;

        for (int i = start; i < end; i++) {
            page.add(function.apply(rows.get(i)));
        }

        page.setTotal(rows.size());

        return page;

    }

    public static <T> Page<T> from(org.springframework.data.domain.Page<T> rows, PageInfo pageInfo) {
        Page<T> page = new Page<>(pageInfo.getPageIndex(), pageInfo.getPageSize());
        rows.forEach(page::add);
        page.setTotal(rows.getTotalElements());
        return page;
    }

    @SuppressWarnings("unchecked")
    public static <E> Page<E> emptyPage(PageInfo pageInfo) {
        Page page = new Page(pageInfo.getPageIndex(), pageInfo.getPageSize());
        page.setTotal(0);
        return page;
    }
}
