package com.barogo.java.delivery.poc.service;

import com.barogo.java.delivery.poc.dto.UserSignupRequestDTO;
import com.barogo.java.delivery.poc.entity.User;
import com.barogo.java.delivery.poc.repository.UserRepository;

public class UserCreator {

	private final UserRepository userRepository;

	public UserCreator(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void signUp(UserSignupRequestDTO requestDTO) {
		User user = User.builder()
			.email(requestDTO.getEmail())
			.password(requestDTO.getPassword())
			.name(requestDTO.getName())
			.build();

		User savedUer = userRepository.save(user);
	}
}
