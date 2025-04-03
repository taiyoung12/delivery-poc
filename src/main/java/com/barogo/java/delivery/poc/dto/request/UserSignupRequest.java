package com.barogo.java.delivery.poc.dto.request;

import com.barogo.java.delivery.poc.dto.validation.ValidPassword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequest {

	@NotBlank(message = "이메일은 필수입니다.")
	private String email;

	@ValidPassword(message = "비밀번호는 최소 12자 이상이며, 대소문자, 숫자, 특수문자를 포함해야 합니다.")
	private String password;

	@NotBlank(message = "이름은 필수입니다.")
	private String name;
}