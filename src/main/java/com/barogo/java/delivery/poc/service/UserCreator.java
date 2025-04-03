package com.barogo.java.delivery.poc.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barogo.java.delivery.poc.dto.request.UserSignupRequest;
import com.barogo.java.delivery.poc.entity.User;
import com.barogo.java.delivery.poc.repository.UserRepository;

@Service
public class UserCreator {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserCreator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void signUp(UserSignupRequest userSignupRequest) {
		String encodePassword = passwordEncoder.encode(userSignupRequest.getPassword());

		User user = User.builder()
			.email(userSignupRequest.getEmail())
			.password(encodePassword)
			.name(userSignupRequest.getName())
			.build();

		userRepository.save(user);
	}
}
