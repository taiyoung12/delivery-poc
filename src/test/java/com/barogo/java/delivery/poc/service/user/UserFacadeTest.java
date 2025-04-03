package com.barogo.java.delivery.poc.service.user;

import static com.barogo.java.delivery.poc.common.code.UserCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.barogo.java.delivery.poc.common.config.token.JwtGenerator;
import com.barogo.java.delivery.poc.common.exception.BaseException;
import com.barogo.java.delivery.poc.common.utils.PasswordMatcher;
import com.barogo.java.delivery.poc.entity.User;
import com.barogo.java.delivery.poc.service.user.UserFacade;
import com.barogo.java.delivery.poc.service.user.UserReader;

@ExtendWith(MockitoExtension.class)
public class UserFacadeTest {

	@Mock
	private UserReader userReader;

	@Mock
	private JwtGenerator jwtGenerator;

	@Mock
	private PasswordMatcher passwordMatcher;

	private UserFacade sut;

	@BeforeEach
	void setUp(){
		sut = new UserFacade(userReader, jwtGenerator, passwordMatcher);
	}

	@Test
	void 매칭되어지는_유저가_있다면_token_발행을_할_수_있다(){
		String email = "barogo@naver.com";
		String password = "encrypted-password";

		User user = User.builder()
			.email(email)
			.password("encrypted-password")
			.name("테스트유저")
			.build();

		when(userReader.findByEmail(email)).thenReturn(user);
		when(passwordMatcher.matches(password, user.getPassword())).thenReturn(true);
		when(jwtGenerator.generateAccessToken(user.getEmail())).thenReturn("MOCKED-TOKEN");

		String actual = sut.userLogin(email, password);

		verify(userReader, times(1)).findByEmail(email);
		verify(passwordMatcher, times(1)).matches(password, user.getPassword());
		verify(jwtGenerator, times(1)).generateAccessToken(user.getEmail());

		assertEquals("MOCKED-TOKEN", actual);
	}

	@Test
	void email에_해당하는_유저가_없다면_BaseException을_던진다() {
		String email = "notfound@barogo.com";
		String password = "any-password";

		when(userReader.findByEmail(email))
			.thenThrow(new BaseException(NOT_FOUND_USER_BY_EMAIL));

		BaseException exception = assertThrows(BaseException.class, () -> {
			sut.userLogin(email, password);
		});

		assertEquals("email 과 일치하는 유저를 찾을 수 없습니다.", exception.getMessage());

		verify(userReader, times(1)).findByEmail(email);
		verify(passwordMatcher, never()).matches(any(), any());
		verify(jwtGenerator, never()).generateAccessToken(any());
	}

	@Test
	void 비밀번호가_일치하지_않는다면_BaseException을_던진다() {
		String email = "barogo@naver.com";
		String inputPassword = "wrong-password";

		User user = User.builder()
			.email(email)
			.password("correct-encrypted-password")
			.name("테스트유저")
			.build();

		when(userReader.findByEmail(email)).thenReturn(user);
		when(passwordMatcher.matches(inputPassword, user.getPassword()))
			.thenReturn(false);

		BaseException exception = assertThrows(BaseException.class, () -> {
			sut.userLogin(email, inputPassword);
		});

		assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());

		verify(userReader, times(1)).findByEmail(email);
		verify(passwordMatcher, times(1)).matches(inputPassword, user.getPassword());
		verify(jwtGenerator, never()).generateAccessToken(any());
	}
}
