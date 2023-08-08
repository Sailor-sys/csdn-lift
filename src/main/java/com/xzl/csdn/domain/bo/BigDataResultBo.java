package com.xzl.csdn.domain.bo;

import lombok.Data;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-19 16:13
 **/
@Data
public class BigDataResultBo<T> {
    private Integer totalSize;

    private Integer pageNumber;

    private Integer pages;

    private List<T> data;

}
