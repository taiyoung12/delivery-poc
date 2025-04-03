package com.barogo.java.delivery.poc.common.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryCode implements Code {
	NOT_FOUND_DELIVERY_BY_ID("DV404", "배송 주문을 조회할 수 없습니다.", HttpStatus.NOT_FOUND),
	NOT_VALIDATED_ADDRESS("DV404", "주소 정합성 검증에 실패했습니다.", HttpStatus.NOT_FOUND);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
