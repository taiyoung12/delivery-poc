package com.barogo.java.delivery.poc.dto.request;

import com.barogo.java.delivery.poc.dto.validation.ValidPassword;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSigninRequest {
	@NotBlank(message = "이메일은 필수입니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수입니다.")
	private String password;
}
