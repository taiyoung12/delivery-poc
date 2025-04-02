package com.barogo.java.delivery.poc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.barogo.java.delivery.poc.dto.request.UserSignupRequest;
import com.barogo.java.delivery.poc.service.UserCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
public class AuthControllerTest {

	@Mock
	private UserCreator userCreator;

	@InjectMocks
	private AuthController authController;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp(RestDocumentationContextProvider restDocumentation) {
		mockMvc = MockMvcBuilders
			.standaloneSetup(authController)
			.apply(documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	void 회원가입_요청이_성공하면_200_상태코드와_성공_응답을_반환한다() throws Exception {
		UserSignupRequest request = new UserSignupRequest();
		request.setEmail("test@example.com");
		request.setPassword("test@example.com");
		request.setName("테스트유저");

		doNothing().when(userCreator).signUp(any(UserSignupRequest.class));

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(jsonPath("$.code").value("US200"))
			.andExpect(jsonPath("$.data").isEmpty())
			.andExpect(jsonPath("$.message").value("회원가입에 성공하였습니다."))
			.andDo(document("auth-signup",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호 (12자 이상)"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터 (없음)")
				)
			));

		verify(userCreator).signUp(any(UserSignupRequest.class));
	}

	@Test
	void Email이_null이라면_회원가입_요청은_400_상태코드를_반환한다() throws Exception {
		UserSignupRequest request = new UserSignupRequest();

		request.setEmail(null);
		request.setPassword("password1234");
		request.setName("바로고");


		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			 .andDo(document("auth-signup-email-null",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())
			));
	}

	@Test
	void password가_12자_이하라면_회원가입_요청은_400_상태코드를_반환한다() throws Exception {
		UserSignupRequest request = new UserSignupRequest();

		request.setEmail("barogo@naver.com");
		request.setPassword("password123");
		request.setName("바로고");

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(document("auth-signup-short-password",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())
			));
	}

	@Test
	void password가_null_이라면_회원가입_요청은_400_상태코드를_반환한다() throws Exception {
		UserSignupRequest request = new UserSignupRequest();

		request.setEmail("barogo@naver.com");
		request.setPassword(null);
		request.setName("바로고");

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(document("auth-signup-null-password",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())
			));
	}

	@Test
	void name이_null_이라면_회원가입_요청은_400_상태코드를_반환한다() throws Exception {
		UserSignupRequest request = new UserSignupRequest();

		request.setEmail("barogo@naver.com");
		request.setPassword("password1234");
		request.setName(null);

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(document("auth-signup-null-name",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())
			));
	}
}