package com.sagatechs.generics.exceptions;

import javax.ejb.ApplicationException;

//https://stackoverflow.com/questions/19563088/a-clear-explanation-of-system-exception-vs-application-exception
@ApplicationException(rollback=true)
public class GeneralAppException extends Exception{

	private static final long serialVersionUID = 1L;

	private Integer httpcode;
	
	public GeneralAppException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneralAppException(String message) {
		super(message);
	}
	
	public GeneralAppException(String message, Integer httpCode) {
		super(message);
		this.httpcode=httpCode;
	}

	
	public Integer getHttpcode() {
		return httpcode;
	}

	@SuppressWarnings("unused")
	public void setHttpcode(Integer httpcode) {
		this.httpcode = httpcode;
	}
	
	
}
