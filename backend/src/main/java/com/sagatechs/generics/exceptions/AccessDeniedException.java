package com.sagatechs.generics.exceptions;

import javax.ejb.ApplicationException;
import java.io.Serializable;

@ApplicationException(rollback = true)
public class AccessDeniedException extends RuntimeException implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}