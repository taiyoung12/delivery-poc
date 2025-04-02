package com.barogo.java.delivery.poc.common.config.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private final String secretKey;
	private final Long accessTokenExpiration;
}