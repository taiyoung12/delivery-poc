package com.barogo.java.delivery.poc.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barogo.java.delivery.poc.common.code.DeliveryCode;
import com.barogo.java.delivery.poc.common.exception.BaseException;
import com.barogo.java.delivery.poc.dto.response.DeliveryReadResponse;
import com.barogo.java.delivery.poc.entity.Delivery;
import com.barogo.java.delivery.poc.repository.DeliveryRepository;

@Service
public class DeliveryReader {

	private final DeliveryRepository deliveryRepository;

	public DeliveryReader(DeliveryRepository deliveryRepository) {
		this.deliveryRepository = deliveryRepository;
	}

	@Transactional(readOnly = true)
	public DeliveryReadResponse findDeliveryBy(
		Long userId,
		LocalDateTime startDate,
		LocalDateTime endDate
	) {
		List<Delivery> deliveries =  deliveryRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
			userId, startDate, endDate
		);

		return DeliveryReadResponse.from(deliveries);
	}

	@Transactional(readOnly = true)
	public Delivery findDeliveryBy(Long id) {
		return deliveryRepository.findById(id)
			.orElseThrow(() -> new BaseException(DeliveryCode.NOT_FOUND_DELIVERY_BY_ID));
	}
}
