package com.xzl.csdn.support;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.xzl.csdn.domain.bo.BigDataResultBo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
public class ApiResult extends HashMap<String, Object> {

    private static final String MSG = "message";
    private static final String STATUS = "status";
    private static final String DATA = "data";

    public ApiResult() {
        // 默认状态为 success
        setStatus(ApiStatus.SUCCESS);
    }

    public void setStatus(ApiStatus status) {
        super.put(STATUS, status.getStatus());
        super.put(MSG, status.getMessage());

    }

    public void setStatus(Integer status) {
        super.put(STATUS, status);
    }

    public void setMessage(String info) {
        super.put(MSG, info);
    }

    public void appendMessage(String msg) {
        super.put(MSG, super.get(MSG) + msg);
    }

    public void setData(Object data) {
        if (data instanceof Page) {
            setPage((Page) data);
        } else {
            super.put(DATA, data);
        }
    }

    public void setPage(Page page) {
        this.setData(new PageResult(page));
    }

    /**
     * 设置分页数据，允许对数据进行二次转换
     */
    public <T, R> void setPage(Page<T> page, Function<T, R> function) {
        PageResult pageResult = new PageResult(page);
        List<R> data = page.stream().map(function).collect(Collectors.toList());
        pageResult.setResult(data);
        this.setData(pageResult);
    }

    /**
     * 对于分页数据，塞到 ApiResult 中时使用此类辅助
     */
    public static class PageResult {
        private List result;
        private Map<String, Object> pageInfo = new HashMap<>();

        public PageResult() {
        }

        public PageResult(Page pageList) {
            setResult(pageList.getResult());

            PageInfo page = new PageInfo(pageList);

            pageInfo.put("pageIndex", page.getPageNum());
            pageInfo.put("pageSize", page.getPageSize());
            pageInfo.put("recordAmount", page.getSize());
            pageInfo.put("recordTotalAmount", page.getTotal());
            pageInfo.put("pagesAmount", page.getPages());
            pageInfo.put("isFirstPage", page.isIsFirstPage());
            pageInfo.put("isLastPage", page.isIsLastPage());
        }

        public PageResult(org.springframework.data.domain.Page pageList, List content) {
            setResult(content);

            pageInfo.put("pageIndex", pageList.getPageable().getPageNumber() + 1);
            pageInfo.put("pageSize", pageList.getPageable().getPageSize());
            pageInfo.put("recordAmount", (pageList.getPageable().getPageNumber() + 1) < pageList.getTotalPages() ? pageList.getPageable().getPageSize() : pageList.getTotalElements() / pageList.getPageable().getPageSize());
            pageInfo.put("recordTotalAmount", pageList.getTotalElements());
            pageInfo.put("pagesAmount", pageList.getTotalPages());
            pageInfo.put("isFirstPage", pageList.getPageable().getPageNumber() == 0);
            pageInfo.put("isLastPage", (pageList.getPageable().getPageNumber() + 1) == pageList.getTotalPages());
        }

        public PageResult(BigDataResultBo obj, List content) {
            setResult(content);

            pageInfo.put("pageIndex", obj.getPageNumber());
            pageInfo.put("recordTotalAmount", obj.getTotalSize());
            pageInfo.put("pagesAmount", obj.getPages());
            pageInfo.put("isFirstPage", obj.getPageNumber() == 1);
            pageInfo.put("isLastPage", obj.getPageNumber() == obj.getPages());
        }

        public List getResult() {
            return result;
        }

        public void setResult(List result) {
            this.result = result;
        }

        public Map<String, Object> getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(Map<String, Object> pageInfo) {
            this.pageInfo = pageInfo;
        }

        /**
         * 得到 map<string, object> 形式的结果
         *
         * @return
         */
        public Map<String, Object> mapTypeInfo() {
            Map<String, Object> result = new HashMap<>();
            result.put("result", getResult());
            result.put("pageInfo", getPageInfo());
            return result;
        }

    }

    /**
     * 屏蔽关键字段
     */
    @Override
    public String put(String key, Object value) {
        if (STATUS.equalsIgnoreCase(key)
                || DATA.equalsIgnoreCase(key)) {
            throw new IllegalArgumentException
                    (key + " is a inner arg, use setter method instead");
        } else {
            super.put(key, value);
            return key;
        }
    }

    public final static int FILL_NULL = 0;
    public final static int FILL_EMPTY = 1;

    public static PageResult filterObject(PageResult page, String[] originalFields, String[] targetFields, ApiResult result, int emptyValue) {
        List data = new ArrayList<>();

        page.getResult().forEach((i) -> {
            data.add(filterObject(i, originalFields, targetFields, result, emptyValue));
        });

        page.setResult(data);

        return page;
    }

    public static PageResult filterObject(Page page, String[] originalFields, ApiResult result) {
        return filterObject(page, originalFields, originalFields, result);
    }

    public static PageResult filterObject(Page page, String[] originalFields, String[] targetFields, ApiResult result) {
        return filterObject(page, originalFields, targetFields, result, FILL_EMPTY);
    }

    public static PageResult filterObject(Page page, String[] originalFields, String[] targetFields, ApiResult result, int emptyValue) {
        PageResult pageResult = new PageResult(page);

        List data = new ArrayList<>();

        page.getResult().forEach((i) -> {
            data.add(filterObject(i, originalFields, targetFields, result, emptyValue));
        });

        pageResult.setResult(data);

        return pageResult;
    }

    /**
     * 将对象中的字段过滤成Map
     *
     * @param object         带信息的对象实例
     * @param originalFields 需要留下来的字段名
     * @param result         ApiResult 对象，如果过程出现异常，信息会写入这里
     * @return 包含了 fields 中表明的字段的 Map，若出现异常则返回 null
     */
    public static Map filterObject(Object object, String[] originalFields, ApiResult result) {
        return filterObject(object, originalFields, originalFields, result);
    }


    /**
     * 将对象中的字段过滤成Map
     *
     * @param object         带信息的对象实例
     * @param targetFields   需要留下来的字段名
     * @param originalFields object的字段名
     * @param result         ApiResult 对象，如果过程出现异常，信息会写入这里
     * @return 包含了 fields 中表明的字段的 Map，若出现异常则返回 null
     */
    public static Map filterObject(Object object, String[] originalFields, String[] targetFields, ApiResult result) {
        return filterObject(object, originalFields, targetFields, result, FILL_EMPTY);
    }

    /**
     * 将对象中的字段过滤成Map
     *
     * @param object         带信息的对象实例
     * @param targetFields   需要留下来的字段名
     * @param originalFields object的字段名
     * @param result         ApiResult 对象，如果过程出现异常，信息会写入这里
     * @param emptyValue     空位采用什么填充方式 {@link #FILL_NULL} 、{@link #FILL_EMPTY}
     * @return 包含了 fields 中表明的字段的 Map，若出现异常则返回 null
     */
    public static Map filterObject(Object object, String[] originalFields, String[] targetFields, ApiResult result, int emptyValue) {

        if (originalFields.length != targetFields.length) {
            throw new IllegalArgumentException("OriginalField length must equals targetFields length");
        }


        String emptyStr;
        switch (emptyValue) {
            case FILL_NULL:
                emptyStr = null;
                break;
            case FILL_EMPTY:
                emptyStr = "";
                break;
            default:
                throw new IllegalArgumentException("unsupported empty value");
        }

        Map<String, Object> map = new HashMap<>();


        for (int i = 0; i < originalFields.length; i++) {
            try {
                Object f = DataBindingHelper.getField(object, originalFields[i]);
                if (f != null) {
                    map.put(targetFields[i], f);
                } else {
                    map.put(targetFields[i], emptyStr);
                }

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put(targetFields[i], emptyStr);
            }
        }

        if (map.size() != targetFields.length) {
            result.setStatus(ApiStatus.INTERNAL_DATA_BINGING);
            throw new IllegalStateException("结果参数数据过滤错误");
        }

        return map;
    }
}
