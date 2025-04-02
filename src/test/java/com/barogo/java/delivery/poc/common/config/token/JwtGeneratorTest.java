package com.barogo.java.delivery.poc.common.config.token;

import org.junit.jupiter.api.Test;

public class JwtGenratorTest {
	private final JwtGenrator sut = new JwtGenrator();

	@Test
	void jwt_token_을_생성_할_수_있다(){
		String userId = "barogo";

		sut.generateToken(userId = userId);
	}
}
