package com.barogo.java.delivery.poc.common.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtCode implements Code {
	FAIL_EXTRACT_SUBJECT("US401", "토큰에서 사용자 정보를 추출할 수 없습니다.", HttpStatus.UNAUTHORIZED);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
