package com.xzl.csdn.domain.bo;

import lombok.Data;

@Data
public class StarWebGetTokenBo {
	private String appKey;

	private String appSecret;
	
	private String username;
	
	private String password;
}
