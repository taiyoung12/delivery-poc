package com.barogo.java.delivery.poc.service;

import com.barogo.java.delivery.poc.common.config.app.annotation.Facade;
import com.barogo.java.delivery.poc.entity.Delivery;

@Facade
public class DeliveryFacade {

	private final DeliveryReader deliveryReader;
	private final DeliveryUpdater deliveryUpdater;

	public DeliveryFacade(DeliveryReader deliveryReader, DeliveryUpdater deliveryUpdater) {
		this.deliveryReader = deliveryReader;
		this.deliveryUpdater = deliveryUpdater;
	}

	public String updateDeliveryAddress(Long deliveryId, String address){
		Delivery delivery = deliveryReader.findDeliveryBy(deliveryId);
		deliveryUpdater.updateDelivery(delivery, address);

		return delivery.getAddress();
	}
}
