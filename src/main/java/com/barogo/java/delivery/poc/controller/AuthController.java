package com.barogo.java.delivery.poc.controller;

import static com.barogo.java.delivery.poc.common.code.AuthCode.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barogo.java.delivery.poc.common.response.Response;
import com.barogo.java.delivery.poc.dto.request.UserSignupRequest;
import com.barogo.java.delivery.poc.service.UserCreator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final UserCreator userCreator;

	public AuthController(UserCreator userCreator) {
		this.userCreator = userCreator;
	}

	@PostMapping("/signup")
	public Response<Void> signup(@Valid @RequestBody UserSignupRequest requestDTO) {
		userCreator.signUp(requestDTO);
		return Response.success(USER_SIGN_UP_SUCCESS);
	}
}