package com.novus.persistence.exceptions;

public class ColumnRegistrationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ColumnRegistrationException(String message) {
		super(message);
	}

	public ColumnRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ColumnRegistrationException(Throwable cause) {
		super(cause);
	}
}
