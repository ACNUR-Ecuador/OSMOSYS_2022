package com.sagatechs.generics.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class AuthorizationException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return super.getMessage();
    }
}