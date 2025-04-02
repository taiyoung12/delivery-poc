package com.barogo.java.delivery.poc.common.config.token;

import static com.barogo.java.delivery.poc.common.code.JwtCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.barogo.java.delivery.poc.common.exception.BaseException;

public class JwtValidatorTest {
	String secretKey = "your-super-secret-key-your-super-secret-key";
	long accessTokenExpiration = 3600000;


	@Mock
	JwtProperties jwtProperties;

	JwtGenerator jwtGenerator;
	JwtValidator sut;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		when(jwtProperties.getSecretKey()).thenReturn(secretKey);
		when(jwtProperties.getAccessTokenExpiration()).thenReturn(accessTokenExpiration);

		jwtGenerator = new JwtGenerator(jwtProperties);
		sut = new JwtValidator(jwtProperties);
	}

	@Test
	void 토큰_검증을_할_수_있다() {
		String userId = "user123";

		String token = jwtGenerator.generateAccessToken(userId);
		boolean actual = sut.validate(token);

		assertThat(actual).isTrue();
	}

	@Test
	void 토큰이_만료되면_검증을_실패할_수_있다() {
		String userId = "user123";
		when(jwtProperties.getAccessTokenExpiration()).thenReturn(-1000L);
		JwtGenerator generator = new JwtGenerator(jwtProperties);

		String token = generator.generateAccessToken(userId);
		boolean actual = sut.validate(token);

		assertThat(actual).isFalse();
	}

	@Test
	void 잘못된_형식의_토큰은_검증에_실패할_수_있다() {
		String invalidToken = "invalid.token.format";

		boolean actual = sut.validate(invalidToken);

		assertThat(actual).isFalse();
	}

	@Test
	void 토큰에서_subject를_추출할_수_있다() {
		String userId = "user123";
		String token = jwtGenerator.generateAccessToken(userId);

		String actual = sut.extractSubject(token);

		assertThat(actual).isEqualTo(userId);
	}

	@Test
	void 잘못된_형식의_토큰에서는_subject_추출에_실패할_수_있다() {
		String invalidToken = "invalid.token.format";

		assertThatThrownBy(() -> sut.extractSubject(invalidToken))
			.isInstanceOf(BaseException.class)
			.hasFieldOrPropertyWithValue("code", FAIL_EXTRACT_SUBJECT);
	}

}