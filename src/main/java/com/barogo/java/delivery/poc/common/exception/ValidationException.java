package com.barogo.java.delivery.poc.common.exception;

import java.util.Map;

import com.barogo.java.delivery.poc.common.code.CommonCode;

import lombok.Getter;

@Getter
public class ValidationException extends BaseException {
	private final Map<String, String> errors;

	public ValidationException(Map<String, String> errors) {
		super(CommonCode.VALIDATION_ERROR);
		this.errors = errors;
	}
}