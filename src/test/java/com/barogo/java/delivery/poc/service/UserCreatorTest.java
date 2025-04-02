package com.barogo.java.delivery.poc.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.barogo.java.delivery.poc.dto.UserSignupRequestDTO;
import com.barogo.java.delivery.poc.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserCreatorTest {

	@Mock
	private UserRepository userRepository;

	private UserCreator sut;

	@BeforeEach
	void setUp() {
		sut = new UserCreator(userRepository);
	}

	@Test
	void requestDTO가_들어오면_userRepository를_호출할_수_있다(){
		String email = "barogo@barogo.com";
		String password = "barogo1234";
		String name = "바로고";

		UserSignupRequestDTO requestDTO = new UserSignupRequestDTO();
		requestDTO.setEmail(email);
		requestDTO.setPassword(password);
		requestDTO.setName(name);

		when(userRepository.save(any())).thenReturn(any());

		sut.signUp(requestDTO);

		verify(userRepository, times(1)).save(any());
	}
}