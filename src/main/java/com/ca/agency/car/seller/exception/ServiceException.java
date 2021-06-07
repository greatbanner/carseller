package com.ca.agency.car.seller.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends Exception {
    
	private HttpStatus httpStatusCode;

    public ServiceException() {
		super();
	}

    public ServiceException(HttpStatus httpStatusCode, String message) {
		super(message);
		this.httpStatusCode = httpStatusCode;
	}

	public HttpStatus getHttpStatusCode() {
		return httpStatusCode;
	}

}
