package com.barogo.java.delivery.poc.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.barogo.java.delivery.poc.entity.Delivery;

public interface DeliveryRepository extends Repository<Delivery, Long> {
	List<Delivery> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
		Long userId,
		LocalDateTime startDate,
		LocalDateTime endDate
	);

	Optional<Delivery> findById(Long id);

	Delivery save(Delivery delivery);
}
