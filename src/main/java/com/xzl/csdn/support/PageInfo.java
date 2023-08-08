package com.xzl.csdn.support;

import lombok.Data;

@Data
public class PageInfo{

    private int pageIndex = 1;
    private int pageSize = 20;

    public PageInfo() {
    }

    public PageInfo(Integer pageIndex, Integer pageSize) {
    	if (null != pageIndex && 0 != pageIndex) {
    		 this.pageIndex = pageIndex;
		}
    	if (null != pageSize && 0 != pageSize) {
    		this.pageSize = pageSize;
		}
    }
}
