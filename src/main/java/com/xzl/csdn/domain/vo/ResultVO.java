package com.xzl.csdn.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liuchao
 */
@Getter
@Setter
@ToString
public class ResultVO<T> {

    private Integer code;

    private String message;

    private T data;

    public ResultVO() {

    }

    public ResultVO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResultVO success(Object data) {
        return new ResultVO(200, "成功", data);
    }

}
