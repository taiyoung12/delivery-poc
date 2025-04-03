package com.barogo.java.delivery.poc.service.delivery;

import static com.barogo.java.delivery.poc.service.fixture.DeliveryFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.barogo.java.delivery.poc.dto.response.DeliveryReadResponse;
import com.barogo.java.delivery.poc.entity.Delivery;
import com.barogo.java.delivery.poc.repository.DeliveryRepository;
import com.barogo.java.delivery.poc.service.delivery.DeliveryReader;

@ExtendWith(MockitoExtension.class)
class DeliveryReaderTest {

	@Mock
	private DeliveryRepository deliveryRepository;

	private DeliveryReader sut;

	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private Long userId;

	@BeforeEach
	void setUp() {
		userId = 1L;
		startDate = LocalDateTime.of(2025, 4, 1, 0, 0);
		endDate = LocalDateTime.of(2025, 4, 3, 23, 59);
		sut = new DeliveryReader(deliveryRepository);
	}

	@Test
	void 기간_내_배송_목록을_조회할_수_있다() {
		List<Delivery> deliveries = createSampleDeliveries();

		when(deliveryRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
			userId, startDate, endDate)).thenReturn(deliveries);

		DeliveryReadResponse response = sut.findDeliveryBy(userId, startDate, endDate);

		assertThat(response).isNotNull();
		assertThat(response.getDeliveries()).hasSize(2);

		verify(deliveryRepository, times(1)).findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
			userId, startDate, endDate);
	}

	@Test
	void 배송_정보가_없는경우_빈_목록을_반환할_수_있다() {
		when(deliveryRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
			userId, startDate, endDate)).thenReturn(Collections.emptyList());

		DeliveryReadResponse response = sut.findDeliveryBy(userId, startDate, endDate);

		assertThat(response.getDeliveries()).isEmpty();

		verify(deliveryRepository, times(1)).findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
			userId, startDate, endDate);
	}
}