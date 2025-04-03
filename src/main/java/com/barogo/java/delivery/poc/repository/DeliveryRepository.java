package com.barogo.java.delivery.poc.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.Repository;

import com.barogo.java.delivery.poc.entity.Delivery;

public interface DeliveryRepository extends Repository<Delivery, Long> {
	List<Delivery> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
		Long userId,
		LocalDateTime startDate,
		LocalDateTime endDate
	);

	void save(Delivery delivery);
}
