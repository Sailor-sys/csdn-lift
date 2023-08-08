package com.xzl.csdn.common.exception;

/**
 * 业务异常
 * @author XZL
 */
public class BusinessException extends RuntimeException {

	/**
	 * 序列化id
	 */

	private Integer errorCode;
	private String errorMessage;

	public BusinessException() {
        super();
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

	public BusinessException(Integer type, String msg){
		super(msg);
		this.errorCode = type;
		this.errorMessage = msg;
	}



	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
