package com.xzl.csdn.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: hechengzhong
 * @Description: 分页数据
 * @date 2023/4/4 11:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<DATA> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<DATA> data;

    /**
     * 当前页码
     */
    private Integer currentPageNo;

    /**
     * 当前返回数据的数量
     */
    private Integer currentPageSize;

    /**
     * 总共多少行
     */
    private Integer totalCount;

    /**
     * 当前数据是否最后一页数据
     */
    private Boolean isLastPage;

}
