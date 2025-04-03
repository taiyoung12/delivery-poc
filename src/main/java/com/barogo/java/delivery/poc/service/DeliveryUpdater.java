package com.barogo.java.delivery.poc.service;

import org.springframework.stereotype.Service;

import com.barogo.java.delivery.poc.entity.Delivery;
import com.barogo.java.delivery.poc.repository.DeliveryRepository;

@Service
public class DeliveryUpdater {
	private final DeliveryRepository deliveryRepository;

	public DeliveryUpdater(DeliveryRepository deliveryRepository) {
		this.deliveryRepository = deliveryRepository;
	}

	public Delivery updateDelivery(
		Delivery delivery,
		String address
	){
		delivery.setAddress(address);
		return deliveryRepository.save(delivery);
	}
}
