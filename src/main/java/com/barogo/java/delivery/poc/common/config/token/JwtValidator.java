package com.barogo.java.delivery.poc.common.config.token;

import static com.barogo.java.delivery.poc.common.code.JwtCode.*;

import java.security.Key;

import com.barogo.java.delivery.poc.common.exception.BaseException;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtValidator {
	private final JwtProperties jwtProperties;
	private final Key secretKey;

	public JwtValidator(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
	}

	public boolean validate(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public String extractSubject(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		} catch (JwtException e) {
			throw new BaseException(FAIL_EXTRACT_SUBJECT);
		}
	}
}
