package com.barogo.java.delivery.poc.dto.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.barogo.java.delivery.poc.validation.PasswordValidator;

import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidatorTest {
	private PasswordValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		validator = new PasswordValidator();
	}

	@Test
	public void 유요한_비밀번호_검증을_할_수_있다() {
		assertTrue(validator.isValid("Password123!", context));
		assertTrue(validator.isValid("Secure@Password123", context));
		assertTrue(validator.isValid("Very$ecureP@ss1", context));
	}

	@Test
	public void null_일_때_검증이_실패할_수_있다() {
		assertFalse(validator.isValid(null, context));
	}

	@Test
	public void 길이가_12_미만일_때_검증이_실패할_수_있다() {
		assertFalse(validator.isValid("Pass123!", context));
		assertFalse(validator.isValid("Short1@", context));
	}

	@Test
	public void 대문자가_포함이_안되어_있다면_검증이_실패할_수_있다() {
		assertFalse(validator.isValid("password123!", context));
		assertFalse(validator.isValid("only_lowercase123!", context));
	}

	@Test
	public void 소문자가_포함이_안되어_있다면_검증이_실패할_수_있다() {
		assertFalse(validator.isValid("PASSWORD123!", context));
		assertFalse(validator.isValid("ONLY_UPPERCASE123!", context));
	}

	@Test
	public void 숫자가_포함이_안되어_있다면_검증이_실패할_수_있다() {
		assertFalse(validator.isValid("PasswordTest!", context));
		assertFalse(validator.isValid("NoDigitsHere@", context));
	}

	@Test
	public void 특수문자가_포함이_안되어_있다면_검증이_실패할_수_있다() {
		assertFalse(validator.isValid("Password123456", context));
		assertFalse(validator.isValid("NoSpecialChars123", context));
	}

	@Test
	public void 길이에_대한_경계값_테스트를_성공할_수_있다() {
		assertTrue(validator.isValid("Abcdefg123!@", context));

		assertFalse(validator.isValid("Abcdef123!@", context));
	}

	@Test
	public void 공백_또한_포함시킬_수_있다() {
		assertTrue(validator.isValid("Password 123!", context));
	}
}
