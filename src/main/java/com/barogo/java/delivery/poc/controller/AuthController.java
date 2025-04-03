package com.barogo.java.delivery.poc.controller;

import static com.barogo.java.delivery.poc.common.code.AuthCode.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barogo.java.delivery.poc.common.response.Response;
import com.barogo.java.delivery.poc.dto.request.UserSigninRequest;
import com.barogo.java.delivery.poc.dto.request.UserSignupRequest;
import com.barogo.java.delivery.poc.service.UserCreator;
import com.barogo.java.delivery.poc.service.UserFacade;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final UserCreator userCreator;
	private final UserFacade userFacade;

	public AuthController(UserCreator userCreator, UserFacade userFacade) {
		this.userCreator = userCreator;
		this.userFacade = userFacade;
	}

	@PostMapping("/signup")
	public Response<Void> signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
		userCreator.signUp(userSignupRequest);
		return Response.success(USER_SIGN_UP_SUCCESS);
	}

	@PostMapping("/signin")
	public Response<String> login(@Valid @RequestBody UserSigninRequest userSigninRequest) {
		String response = userFacade.userLogin(
			userSigninRequest.getEmail(),
			userSigninRequest.getPassword()
		);

		return Response.success(response);
	}
}