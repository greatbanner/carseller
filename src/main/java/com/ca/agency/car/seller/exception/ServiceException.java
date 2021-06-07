package com.ca.agency.car.seller.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends Exception {
    
	private final HttpStatus httpStatusCode;

    public ServiceException() {
		super();
		this.httpStatusCode = null;
	}

    public ServiceException(HttpStatus httpStatusCode, String message) {
		super(message);
		this.httpStatusCode = httpStatusCode;
	}

	public HttpStatus getHttpStatusCode() {
		return httpStatusCode;
	}

}
