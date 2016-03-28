package com.poison.eagle.utils.craw;


public class SaveNetImageResult {
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	private String message = "";			   //返回消息
	private boolean isSuccess;	               //结果是否成功
}
