package com.barogo.java.delivery.poc.service;

import org.springframework.stereotype.Service;

import com.barogo.java.delivery.poc.dto.request.UserSignupRequest;
import com.barogo.java.delivery.poc.entity.User;
import com.barogo.java.delivery.poc.repository.UserRepository;

@Service
public class UserCreator {

	private final UserRepository userRepository;

	public UserCreator(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void signUp(UserSignupRequest requestDTO) {
		User user = User.builder()
			.email(requestDTO.getEmail())
			.password(requestDTO.getPassword())
			.name(requestDTO.getName())
			.build();

		User savedUer = userRepository.save(user);
	}
}
