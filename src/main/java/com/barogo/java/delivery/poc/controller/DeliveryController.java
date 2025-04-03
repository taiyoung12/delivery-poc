package com.barogo.java.delivery.poc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barogo.java.delivery.poc.common.config.app.annotation.UserEmail;
import com.barogo.java.delivery.poc.common.response.Response;
import com.barogo.java.delivery.poc.dto.request.DeliveryReadRequest;
import com.barogo.java.delivery.poc.dto.response.DeliveryReadResponse;
import com.barogo.java.delivery.poc.service.delivery.DeliveryReader;
import com.barogo.java.delivery.poc.service.user.UserReader;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/delivery")
public class DeliveryController {
	private final DeliveryReader deliveryReader;
	private final UserReader userReader;

	public DeliveryController(DeliveryReader deliveryReader, UserReader userReader) {
		this.deliveryReader = deliveryReader;
		this.userReader = userReader;
	}

	@GetMapping
	public Response<DeliveryReadResponse> findDelivery(
		@UserEmail String email,
		@Valid @ModelAttribute DeliveryReadRequest request
		) {
		Long userId = userReader.findByEmail(email).getId();

		DeliveryReadResponse deliveryReadResponse = deliveryReader.findDeliveryBy(
			userId,
			request.getStartDate(),
			request.getEndDate()
		);

		return Response.success(deliveryReadResponse);
	}
}
