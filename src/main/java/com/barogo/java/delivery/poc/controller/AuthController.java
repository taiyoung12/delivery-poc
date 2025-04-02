package com.barogo.java.delivery.poc.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barogo.java.delivery.poc.common.response.Response;
import com.barogo.java.delivery.poc.dto.request.UserSignupRequest;
import com.barogo.java.delivery.poc.service.UserCreator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserCreator userCreator;

	public AuthController(UserCreator userCreator) {
		this.userCreator = userCreator;
	}

	@PostMapping("/signup")
	public Response<User> signup(@Valid @RequestBody UserSignupRequest requestDTO) {
		User user = userCreator.signUp(requestDTO);
		return Response.success(user);
	}
}