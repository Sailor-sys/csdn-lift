package com.xzl.csdn.common.http.response;

import lombok.Data;

import java.util.List;

/**
 * @Author tfdeng
 * @Description
 * @Date 2023/01/06/15:43
 * @Version 1.0
 */
@Data
public class BigDataResponseDTO<T> {
    private String code;
    private String msg;
    private List<T> data;
}
