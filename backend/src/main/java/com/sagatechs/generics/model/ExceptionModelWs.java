package com.sagatechs.generics.model;
public class ExceptionModelWs {
	
	private int code;
	private String message;
	
	
	public ExceptionModelWs(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}