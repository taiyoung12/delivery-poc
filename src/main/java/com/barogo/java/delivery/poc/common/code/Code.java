package com.barogo.java.delivery.poc.common.code;

import org.springframework.http.HttpStatus;

public interface Code {
	String getCode();

	String getMessage();

	HttpStatus getHttpStatus();
}