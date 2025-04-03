package com.barogo.java.delivery.poc.controller;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.time.LocalDateTime;
import java.util.List;

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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.barogo.java.delivery.poc.common.exception.GlobalExceptionHandler;
import com.barogo.java.delivery.poc.dto.response.DeliveryReadResponse;
import com.barogo.java.delivery.poc.entity.User;
import com.barogo.java.delivery.poc.service.DeliveryReader;
import com.barogo.java.delivery.poc.service.UserReader;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
public class DeliveryControllerTest {
	@Mock
	private DeliveryReader deliveryReader;

	@Mock
	private UserReader userReader;

	@InjectMocks
	private DeliveryController deliveryController;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp(RestDocumentationContextProvider restDocumentation) {
		mockMvc = MockMvcBuilders
			.standaloneSetup(deliveryController)
			.setCustomArgumentResolvers(new TestUserEmailResolver())
			.setControllerAdvice(new GlobalExceptionHandler())
			.apply(documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	void 배송_조회_요청이_성공하면_200_상태코드와_배송_목록을_반환할_수_있다() throws Exception {
		LocalDateTime start = LocalDateTime.of(2025, 4, 1, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 4, 3, 23, 59);

		DeliveryReadResponse response = DeliveryReadResponse.builder()
			.deliveries(List.of(
				DeliveryReadResponse.DeliveryItem.builder()
					.status("READY")
					.address("서울시 동작구 노량진로 100")
					.build(),
				DeliveryReadResponse.DeliveryItem.builder()
					.status("READY")
					.address("서울시 영등포구 여의대로 108")
					.build()
			))
			.build();

		User user = User.builder().build();
		ReflectionTestUtils.setField(user, "id", 1L);
		when(userReader.findByEmail(anyString()))
			.thenReturn(user);

		when(deliveryReader.findDeliveryBy(anyLong(), any(), any()))
			.thenReturn(response);

		mockMvc.perform(get("/api/v1/delivery")
				.param("startDate", start.toString())
				.param("endDate", end.toString())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("CM200"))
			.andExpect(jsonPath("$.message").value("성공"))
			.andExpect(jsonPath("$.data.deliveries").isArray())
			.andDo(document("delivery-find",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("startDate").optional().description("조회 시작일 (ISO 8601 형식, 예: 2024-04-01T00:00:00)"),
					parameterWithName("endDate").optional().description("조회 종료일 (ISO 8601 형식, 예: 2024-04-03T23:59:59)")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("배송 데이터"),
					fieldWithPath("data.deliveries").type(JsonFieldType.ARRAY).description("배송 목록"),
					fieldWithPath("data.deliveries[].status").type(JsonFieldType.STRING).description("배송 상태 (예: READY, DELIVERED 등)"),
					fieldWithPath("data.deliveries[].address").type(JsonFieldType.STRING).description("배송지 주소")
				)
			));
	}

	@Test
	void 조회_기간이_3일을_초과하면_400에러와_예외메시지를_반환할_수_있다() throws Exception {
		LocalDateTime start = LocalDateTime.of(2025, 4, 1, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 4, 5, 0, 0); // ✅ 4일 차이

		mockMvc.perform(get("/api/v1/delivery")
				.param("startDate", start.toString())
				.param("endDate", end.toString())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(document("delivery-find-date-range-invalid",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("startDate").description("조회 시작일 (ISO 8601 형식)"),
					parameterWithName("endDate").description("조회 종료일 (ISO 8601 형식)")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("유효성 에러 상세 정보"),
					fieldWithPath("data.validDateRange").type(JsonFieldType.STRING).description("날짜 유효성 검사 실패 메시지")
				)
			));
	}
}
