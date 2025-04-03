package com.barogo.java.delivery.poc.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.barogo.java.delivery.poc.entity.Delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryReadResponse {

	private List<DeliveryItem> deliveries;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DeliveryItem {
		private String status;
		private String address;
	}

	public static DeliveryReadResponse from(List<Delivery> deliveries) {
		List<DeliveryItem> items = deliveries.stream()
			.map(delivery -> DeliveryItem.builder()
				.status(delivery.getStatus().name())
				.address(delivery.getAddress())
				.build())
			.collect(Collectors.toList());

		return DeliveryReadResponse.builder()
			.deliveries(items)
			.build();
	}
}