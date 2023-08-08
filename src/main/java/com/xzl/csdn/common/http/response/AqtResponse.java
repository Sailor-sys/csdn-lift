package com.xzl.csdn.common.http.response;

import java.io.Serializable;

/**
 * @author tfDeng
 * @description:
 * @date 2020/12/08
 **/
public class AqtResponse<DATA> implements Serializable {
    private static final long serialVersionUID = -3969140103608206600L;
    private int code;
    private String message;
    private DATA data;

    public AqtResponse() {
    }

    public AqtResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DATA getData() {
        return this.data;
    }

    public void setData(DATA data) {
        this.data = data;
    }

}
