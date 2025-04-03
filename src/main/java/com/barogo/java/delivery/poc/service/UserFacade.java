package com.barogo.java.delivery.poc.service;

import static com.barogo.java.delivery.poc.common.code.UserCode.*;

import com.barogo.java.delivery.poc.common.config.app.annotation.Facade;
import com.barogo.java.delivery.poc.common.config.token.JwtGenerator;
import com.barogo.java.delivery.poc.common.exception.BaseException;
import com.barogo.java.delivery.poc.common.utils.PasswordMatcher;
import com.barogo.java.delivery.poc.entity.User;

@Facade
public class UserFacade {
	private final UserReader userReader;

	private final JwtGenerator jwtGenerator;

	private final PasswordMatcher passwordMatcher;

	public UserFacade(UserReader userReader, JwtGenerator jwtGenerator, PasswordMatcher passwordMatcher) {
		this.userReader = userReader;
		this.jwtGenerator = jwtGenerator;
		this.passwordMatcher = passwordMatcher;
	}

	String userLogin(String email, String password){
		User user = userReader.findByEmail(email);

		if (!passwordMatcher.matches(password, user.getPassword())) {
			throw new BaseException(NOT_MATCHED_USER_PASSWORD);
		}

		return jwtGenerator.generateAccessToken(user.getEmail());
	}
}
