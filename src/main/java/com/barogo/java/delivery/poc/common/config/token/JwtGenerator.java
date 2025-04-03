package com.barogo.java.delivery.poc.common.config.token;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerator {
	private final JwtProperties jwtProperties;
	private final Key secretKey;

	public JwtGenerator(
		JwtProperties jwtProperties
	) {
		this.jwtProperties = jwtProperties;
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
	}

	public String generateAccessToken(String subject) {
		long now = System.currentTimeMillis();
		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(new Date(now))
			.setExpiration(new Date(now + jwtProperties.getAccessTokenExpiration()))
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}
}
