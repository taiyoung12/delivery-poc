package com.barogo.java.delivery.poc.service.delivery;

import static com.barogo.java.delivery.poc.common.code.DeliveryCode.*;

import com.barogo.java.delivery.poc.common.config.app.annotation.Facade;
import com.barogo.java.delivery.poc.common.exception.BaseException;
import com.barogo.java.delivery.poc.entity.Delivery;
import com.barogo.java.delivery.poc.validation.AddressValidator;

@Facade
public class DeliveryFacade {

	private final DeliveryReader deliveryReader;
	private final DeliveryUpdater deliveryUpdater;

	private final AddressValidator addressValidator;

	public DeliveryFacade(DeliveryReader deliveryReader, DeliveryUpdater deliveryUpdater,
		AddressValidator addressValidator) {
		this.deliveryReader = deliveryReader;
		this.deliveryUpdater = deliveryUpdater;
		this.addressValidator = addressValidator;
	}

	public String updateDeliveryAddress(Long userId, Long deliveryId, String address){
		if(!addressValidator.isValid(address)){
			throw new BaseException(NOT_VALIDATED_ADDRESS);
		}

		Delivery delivery = deliveryReader.findDeliveryBy(deliveryId);
		if(!delivery.getUserId().equals(userId)){
			throw new BaseException(NOT_ALLOWED_UPDATE_DELIVERY_ADDRESS);
		}

		deliveryUpdater.updateDelivery(delivery, address);

		return delivery.getAddress();
	}
}
