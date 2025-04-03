package com.barogo.java.delivery.poc.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.barogo.java.delivery.poc.enums.DeliveryStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "delivery")
@Getter
@Setter
public class Delivery extends BaseEntity {

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeliveryStatus status = DeliveryStatus.READY;
}