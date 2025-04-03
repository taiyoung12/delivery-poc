package com.barogo.java.delivery.poc.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryUpdateRequest {

	@NotNull(message = "배송 ID는 필수입니다.")
	private Long deliveryId;

	@NotNull(message = "주소는 필수입니다.")
	private String address;
}