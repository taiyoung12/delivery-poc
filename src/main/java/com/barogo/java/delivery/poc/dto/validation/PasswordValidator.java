package com.barogo.java.delivery.poc.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null &&
			value.length() >= 12 &&
			value.matches(".*[A-Z].*") &&
			value.matches(".*[a-z].*") &&
			value.matches(".*\\d.*") &&
			value.matches(".*[!@#$%^&()].*");
	}
}