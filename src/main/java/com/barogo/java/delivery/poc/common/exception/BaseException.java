package com.barogo.java.delivery.poc.common.exception;

import com.barogo.java.delivery.poc.common.code.Code;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private final Code code;

	public BaseException(Code code) {
		super(code.getMessage());
		this.code = code;
	}

	public BaseException(Code code, String message) {
		super(message);
		this.code = code;
	}
}
