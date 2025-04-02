package com.barogo.java.delivery.poc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequestDTO {

	@NotBlank(message = "이메일은 필수입니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(min = 12, message = "비밀번호는 최소 12자 이상이어야 합니다.")
	private String password;

	@NotBlank(message = "이름은 필수입니다.")
	private String name;
}