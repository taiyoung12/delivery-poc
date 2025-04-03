package com.barogo.java.delivery.poc.controller;

import static com.barogo.java.delivery.poc.common.code.DeliveryCode.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.barogo.java.delivery.poc.common.exception.BaseException;
import com.barogo.java.delivery.poc.common.exception.GlobalExceptionHandler;
import com.barogo.java.delivery.poc.dto.request.DeliveryUpdateRequest;
import com.barogo.java.delivery.poc.dto.response.DeliveryReadResponse;
import com.barogo.java.delivery.poc.entity.User;
import com.barogo.java.delivery.poc.service.delivery.DeliveryFacade;
import com.barogo.java.delivery.poc.service.delivery.DeliveryReader;
import com.barogo.java.delivery.poc.service.user.UserReader;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
public class DeliveryControllerTest {

	@Mock
	private DeliveryFacade deliveryFacade;
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

	@Test
	void 배송지_수정_요청이_성공하면_200_상태코드와_수정된_주소를_반환할_수_있다() throws Exception {
		String email = "test@barogo.com";
		Long deliveryId = 1L;
		String newAddress = "서울시 마포구 서강로 123";

		User user = User.builder().build();
		ReflectionTestUtils.setField(user, "id", 1L);

		DeliveryUpdateRequest request = new DeliveryUpdateRequest();
		request.setDeliveryId(deliveryId);
		request.setAddress(newAddress);

		when(userReader.findByEmail(anyString())).thenReturn(user);
		when(deliveryFacade.updateDeliveryAddress(user.getId(), deliveryId, newAddress))
			.thenReturn(newAddress);

		mockMvc.perform(put("/api/v1/delivery")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("CM200"))
			.andExpect(jsonPath("$.message").value("성공"))
			.andExpect(jsonPath("$.data").value(newAddress))
			.andDo(document("delivery-update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("deliveryId").type(JsonFieldType.NUMBER).description("배송 ID"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("변경할 배송지 주소")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data").type(JsonFieldType.STRING).description("수정된 배송지 주소")
				)
			));
	}

	@Test
	void 존재하지_않는_배송ID면_404에러와_예외메시지를_반환할_수_있다() throws Exception {
		Long userId = 1L;
		Long deliveryId = 999L;
		String newAddress = "서울시 송파구 문정동 102-1";

		User user = User.builder().build();
		ReflectionTestUtils.setField(user, "id", userId);

		DeliveryUpdateRequest request = new DeliveryUpdateRequest();
		request.setDeliveryId(deliveryId);
		request.setAddress(newAddress);

		when(userReader.findByEmail(anyString())).thenReturn(user);
		when(deliveryFacade.updateDeliveryAddress(userId, deliveryId, newAddress))
			.thenThrow(new BaseException(NOT_FOUND_DELIVERY_BY_ID));

		mockMvc.perform(put("/api/v1/delivery")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value("DV404"))
			.andExpect(jsonPath("$.message").value("배송 주문을 조회할 수 없습니다."))
			.andDo(document("delivery-update-not-found",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("deliveryId").type(JsonFieldType.NUMBER).description("수정할 배송 ID"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("변경할 주소")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
					fieldWithPath("data").type(JsonFieldType.STRING).description("에러 메시지")
				)
			));
	}

	@Test
	void 주소_정합성_검증에_실패하면_404에러와_예외메시지를_반환할_수_있다() throws Exception {
		Long userId = 1L;
		Long deliveryId = 10L;
		String newAddress = "!!!";

		User user = User.builder().build();
		ReflectionTestUtils.setField(user, "id", userId);

		DeliveryUpdateRequest request = new DeliveryUpdateRequest();
		request.setDeliveryId(deliveryId);
		request.setAddress(newAddress);

		when(userReader.findByEmail(anyString())).thenReturn(user);
		when(deliveryFacade.updateDeliveryAddress(userId, deliveryId, newAddress))
			.thenThrow(new BaseException(NOT_VALIDATED_ADDRESS));

		mockMvc.perform(put("/api/v1/delivery")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value("DV404"))
			.andExpect(jsonPath("$.message").value("주소 정합성 검증에 실패했습니다."))
			.andDo(document("delivery-update-invalid-address",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("deliveryId").type(JsonFieldType.NUMBER).description("수정할 배송 ID"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("변경할 주소")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
					fieldWithPath("data").type(JsonFieldType.STRING).description("에러 메시지")
				)
			));
	}

	@Test
	void 본인의_배송이_아니라면_403에러와_예외메시지를_반환할_수_있다() throws Exception {
		Long userId = 1L;
		Long deliveryId = 20L;
		String newAddress = "서울시 성동구 성수동 1가";

		User user = User.builder().build();
		ReflectionTestUtils.setField(user, "id", userId);

		DeliveryUpdateRequest request = new DeliveryUpdateRequest();
		request.setDeliveryId(deliveryId);
		request.setAddress(newAddress);

		when(userReader.findByEmail(anyString())).thenReturn(user);
		when(deliveryFacade.updateDeliveryAddress(userId, deliveryId, newAddress))
			.thenThrow(new BaseException(NOT_ALLOWED_UPDATE_DELIVERY_ADDRESS));

		mockMvc.perform(put("/api/v1/delivery")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.code").value("DV401"))
			.andExpect(jsonPath("$.message").value("주소를 변경할 권한이 없습니다."))
			.andDo(document("delivery-update-not-allowed",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("deliveryId").type(JsonFieldType.NUMBER).description("수정할 배송 ID"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("변경할 주소")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
					fieldWithPath("data").type(JsonFieldType.STRING).description("에러 메시지")
				)
			));
	}
}
