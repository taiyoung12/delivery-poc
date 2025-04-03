package com.barogo.java.delivery.poc.service.delivery;

import static com.barogo.java.delivery.poc.common.code.DeliveryCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.barogo.java.delivery.poc.common.exception.BaseException;
import com.barogo.java.delivery.poc.entity.Delivery;
import com.barogo.java.delivery.poc.service.delivery.DeliveryFacade;
import com.barogo.java.delivery.poc.service.delivery.DeliveryReader;
import com.barogo.java.delivery.poc.service.delivery.DeliveryUpdater;
import com.barogo.java.delivery.poc.validation.AddressValidator;

public class DeliveryFacadeTest {

	@Mock
	private DeliveryReader deliveryReader;

	@Mock
	private DeliveryUpdater deliveryUpdater;

	@Mock
	private AddressValidator addressValidator;

	private DeliveryFacade sut;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		sut = new DeliveryFacade(deliveryReader, deliveryUpdater, addressValidator);
	}

	@Test
	void 배송지_수정을_호출하면_reader와_update를_호출할_수_있다() {
		Long userId = 1L;
		Long deliveryId = 1L;
		String newAddress = "서울시 송파구 신천동 88";

		Delivery delivery = new Delivery();
		delivery.setAddress("기존 주소");
		delivery.setUserId(userId);

		when(addressValidator.isValid(newAddress)).thenReturn(true);
		when(deliveryReader.findDeliveryBy(deliveryId)).thenReturn(delivery);

		sut.updateDeliveryAddress(userId, deliveryId, newAddress);

		verify(deliveryReader, times(1)).findDeliveryBy(deliveryId);
		verify(deliveryUpdater, times(1)).updateDelivery(delivery, newAddress);
	}

	@Test
	void 요청온_배송지_주소가_잘못된_주소라면_예외를_처리할_수_있다() {
		Long userId = 1L;
		Long deliveryId = 1L;
		String newAddress = "서울시 송파구 신천동 88";

		Delivery delivery = new Delivery();
		delivery.setAddress("잘못된 주소");

		when(deliveryReader.findDeliveryBy(deliveryId)).thenReturn(delivery);
		when(addressValidator.isValid(newAddress)).thenReturn(false);

		assertThrows(BaseException.class, () -> {
			sut.updateDeliveryAddress(userId, deliveryId, newAddress);
		});

		verify(deliveryReader, never()).findDeliveryBy(deliveryId);
		verify(deliveryUpdater, never()).updateDelivery(delivery, newAddress);
	}

	@Test
	void 유저_본인이_주문한_배송이_아니라면_주소지를_변경할_수_없다() {
		Long userId = 1L;
		Long deliveryId = 1L;
		String newAddress = "서울시 송파구 신천동 88";

		Delivery delivery = new Delivery();
		delivery.setAddress("잘못된 주소");
		delivery.setUserId(2L);

		when(deliveryReader.findDeliveryBy(deliveryId)).thenReturn(delivery);
		when(addressValidator.isValid(newAddress)).thenReturn(true);

		assertThrows(BaseException.class, () -> {
			sut.updateDeliveryAddress(userId, deliveryId, newAddress);
		});

		verify(deliveryReader, times(1)).findDeliveryBy(deliveryId);
		verify(deliveryUpdater, never()).updateDelivery(delivery, newAddress);
	}

	@Test
	void 존재하지_않는_배송ID라면_예외를_던진다() {
		Long userId = 1L;
		Long invalidId = 999L;
		String newAddress = "서울시 동작구 장승배기로 210";

		when(addressValidator.isValid(newAddress)).thenReturn(true);
		when(deliveryReader.findDeliveryBy(invalidId))
			.thenThrow(new BaseException(NOT_FOUND_DELIVERY_BY_ID));


		assertThrows(BaseException.class, () -> {
			sut.updateDeliveryAddress(userId, invalidId, newAddress);
		});

		verify(deliveryReader).findDeliveryBy(invalidId);
		verify(deliveryUpdater, never()).updateDelivery(any(), any());
	}
}