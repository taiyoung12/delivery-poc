package com.barogo.java.delivery.poc.common.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.barogo.java.delivery.poc.common.code.CommonCode;
import com.barogo.java.delivery.poc.common.response.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<Response<Object>> handleBusinessException(BaseException e) {
		log.error("Business exception occurred", e);
		Response<Object> response;

		if (e instanceof ValidationException) {
			response = Response.error(e.getCode(), ((ValidationException)e).getErrors());
		} else {
			response = Response.error(e.getCode(), e.getMessage());
		}

		return new ResponseEntity<>(response, e.getCode().getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response<Map<String, String>>> handleValidationExceptions(
		MethodArgumentNotValidException ex) {

		Map<String, String> errors = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				fieldError -> {
					fieldError.getDefaultMessage();
					return fieldError.getDefaultMessage();
				}
			));

		Response<Map<String, String>> response = Response.error(
			CommonCode.VALIDATION_ERROR,
			errors
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response<String>> handleAllExceptions(Exception e) {
		log.error("Unexpected error occurred", e);
		return new ResponseEntity<>(
			Response.error(CommonCode.INTERNAL_ERROR, e.getMessage()),
			HttpStatus.INTERNAL_SERVER_ERROR
		);
	}
}