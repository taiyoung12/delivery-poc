package com.barogo.java.delivery.poc.common.config.token;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtGeneratorTest {
	private final String secretKey = "barogo-test-secret-key-barogo-test-secret-key";
	private final long accessTokenExpiration = 60*10*1000;
	@Mock
	JwtProperties jwtProperties;

	JwtGenerator sut;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(jwtProperties.getSecretKey()).thenReturn(secretKey);
		when(jwtProperties.getAccessTokenExpiration()).thenReturn(accessTokenExpiration);

		sut = new JwtGenerator(jwtProperties);
	}

	@Test
	void accessToken을_생성할_수_있다() {
		String subject = "userId";
		String actual = sut.generateAccessToken(subject);

		assertThat(actual).isNotEmpty();
	}

	@Test
	void accessToken에_subject_userId_가_포함_될_수_있다() {
		String subject = "user123";

		String token = sut.generateAccessToken(subject);

		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey.getBytes())
			.build()
			.parseClaimsJws(token)
			.getBody();

		assertThat(claims.getSubject()).isEqualTo(subject);
	}

	@Test
	void accessToken의_만료_시간을_설정할_수_있다() {
		String subject = "user123";
		String token = sut.generateAccessToken(subject);

		Claims claims = Jwts.parserBuilder()
			.setSigningKey(jwtProperties.getSecretKey().getBytes())
			.build()
			.parseClaimsJws(token)
			.getBody();

		long expectedExpiration = System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration();
		long actualExpiration = claims.getExpiration().getTime();

		assertThat(Math.abs(expectedExpiration - actualExpiration))
			.isLessThan(1000);
	}
}