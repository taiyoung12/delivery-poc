package com.barogo.java.delivery.poc.service.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.barogo.java.delivery.poc.common.exception.BaseException;
import com.barogo.java.delivery.poc.entity.User;
import com.barogo.java.delivery.poc.repository.UserRepository;
import com.barogo.java.delivery.poc.service.user.UserReader;

@ExtendWith(MockitoExtension.class)
public class UserReaderTest {
	@Mock
	private UserRepository userRepository;

	private UserReader sut;

	@BeforeEach
	void setUp() {
		sut = new UserReader(
			userRepository
		);
	}

	@Test
	void email에_해당하는_유저가_있다면_유저를_반환할_수_있다(){
		String email = "test@example.com";
		User user = User.builder()
			.email(email)
			.password("encrypted-password")
			.name("테스트유저")
			.build();

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		User actual = sut.findByEmail(email);

		verify(userRepository, times(1)).findByEmail(email);

		assertEquals(user.getEmail(), actual.getEmail());
		assertEquals(user.getName(), actual.getName());
	}

	@Test
	void email에_해당하는_유저가_없다면_에러_메시지를_반환할_수_있다(){
		String email = "nonexistent@example.com";
		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		BaseException exception = assertThrows(BaseException.class, () -> {
			sut.findByEmail(email);
		});

		verify(userRepository, times(1)).findByEmail(email);

		assertEquals("email 과 일치하는 유저를 찾을 수 없습니다.", exception.getMessage());
	}
}
