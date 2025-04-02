package com.barogo.java.delivery.poc.common.config.token;

import java.util.Date;

import io.jsonwebtoken.Jwts;

public class JwtGenrator {
	private final String secret = "your-secret-key";
	private final long expiration = 1000 * 60 * 60L;

	public String generateToken(String userId) {
		return Jwts.builder()
			.setSubject(userId)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.compact();
	}
}
