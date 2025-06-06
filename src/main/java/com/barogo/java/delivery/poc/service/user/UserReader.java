package com.barogo.java.delivery.poc.service.user;

import static com.barogo.java.delivery.poc.common.code.UserCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barogo.java.delivery.poc.common.exception.BaseException;
import com.barogo.java.delivery.poc.entity.User;
import com.barogo.java.delivery.poc.repository.UserRepository;

@Service
public class UserReader {
	private final UserRepository userRepository;

	public UserReader(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public User findByEmail(String email){
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new BaseException(NOT_FOUND_USER_BY_EMAIL));
	}
}
