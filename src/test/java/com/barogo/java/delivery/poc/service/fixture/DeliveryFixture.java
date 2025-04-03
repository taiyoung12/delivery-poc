package com.barogo.java.delivery.poc.service.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.barogo.java.delivery.poc.entity.Delivery;
import com.barogo.java.delivery.poc.enums.DeliveryStatus;

public class DeliveryFixture {
	public static List<Delivery> createSampleDeliveries() {
		Delivery delivery1 = new Delivery();
		delivery1.setUserId(1L);
		delivery1.setAddress("서울시 강남구 테헤란로 123");
		delivery1.setStatus(DeliveryStatus.PROGRESSING);

		Delivery delivery2 = new Delivery();
		delivery2.setUserId(1L);
		delivery2.setAddress("서울시 서초구 방배로 456");
		delivery2.setStatus(DeliveryStatus.DONE);

		return Arrays.asList(delivery1, delivery2);
	}

}
