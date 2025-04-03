package com.barogo.java.delivery.poc.validation;

import org.springframework.stereotype.Component;

@Component
public class AddressValidator {

	// TODO: 실제 주소 유효성 검사는 외부 API 연동
	public boolean isValid(String address) {
		return true;
	}
}
