package com.barogo.java.delivery.poc.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barogo.java.delivery.poc.common.config.app.annotation.UserEmail;
import com.barogo.java.delivery.poc.common.response.Response;
import com.barogo.java.delivery.poc.dto.response.DeliveryReadResponse;
import com.barogo.java.delivery.poc.service.DeliveryReader;
import com.barogo.java.delivery.poc.service.UserReader;

@RestController
@RequestMapping("/api/v1")
public class DeliveryController {
	private final DeliveryReader deliveryReader;
	private final UserReader userReader;

	public DeliveryController(DeliveryReader deliveryReader, UserReader userReader) {
		this.deliveryReader = deliveryReader;
		this.userReader = userReader;
	}

	@GetMapping("/deliveries")
	public Response<DeliveryReadResponse> findDelivery(
		@UserEmail String email,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

		Long userId = userReader.findByEmail(email).getId();

		DeliveryReadResponse deliveryReadResponse = deliveryReader.findDeliveryBy(userId, startDate, endDate);

		return Response.success(deliveryReadResponse);
	}
}
