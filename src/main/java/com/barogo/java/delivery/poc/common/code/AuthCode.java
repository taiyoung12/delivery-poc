package com.barogo.java.delivery.poc.common.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthCode implements Code{
	USER_SIGN_UP_SUCCESS("US200", "회원가입에 성공하였습니다.", HttpStatus.OK);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
