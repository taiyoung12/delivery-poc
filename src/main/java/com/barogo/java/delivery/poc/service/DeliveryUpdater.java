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

	void updateDelivery(
		Delivery delivery,
		String address
	){
		delivery.setAddress(address);
		deliveryRepository.save(delivery);
	}
}
