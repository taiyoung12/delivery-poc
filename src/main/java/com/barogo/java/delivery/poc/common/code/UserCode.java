package com.barogo.java.delivery.poc.common.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserCode implements Code {
	NOT_FOUND_USER_BY_EMAIL("US404", "email 과 일치하는 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
