package com.barogo.java.delivery.poc.dto.request;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class DeliveryReadRequestTest {

	@Test
	void 두_날짜가_모두_null이면_현재시간부터_3일전까지_기간으로_설정할_수_있다() {
		DeliveryReadRequest request = new DeliveryReadRequest();

		LocalDateTime startDate = request.getStartDate();
		LocalDateTime endDate = request.getEndDate();

		Long daysBetween = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
		assertThat(daysBetween).isEqualTo(3);
	}

	@Test
	void 시작일만_null이면_종료일로부터_3일전으로_자동_설정할_수_있다() {
		DeliveryReadRequest request = new DeliveryReadRequest();
		LocalDateTime endDate = LocalDateTime.of(2025, 4, 1, 12, 0);
		request.setEndDate(endDate);

		LocalDateTime startDate = request.getStartDate();

		LocalDateTime expectedStartDate = endDate.minusDays(3);
		assertThat(startDate).isEqualTo(expectedStartDate);
	}

	@Test
	void 종료일만_null이면_시작일로부터_3일후로_자동_설정할_수_있다() {
		DeliveryReadRequest request = new DeliveryReadRequest();
		LocalDateTime startDate = LocalDateTime.of(2025, 4, 1, 12, 0);
		request.setStartDate(startDate);

		LocalDateTime endDate = request.getEndDate();

		LocalDateTime expectedEndDate = startDate.plusDays(3);
		assertThat(endDate).isEqualTo(expectedEndDate);
	}

	@Test
	void 조회_기간이_3일_이하면_유효성_검사를_통과할_수_있다() {
		DeliveryReadRequest request = new DeliveryReadRequest();
		LocalDateTime startDate = LocalDateTime.of(2025, 4, 1, 12, 0);
		LocalDateTime endDate = LocalDateTime.of(2025, 4, 3, 12, 0);
		request.setStartDate(startDate);
		request.setEndDate(endDate);

		assertThat(request.isValidDateRange()).isTrue();
	}

	@Test
	void 조회_기간이_3일을_초과하면_유효성_검사를_통과할_수_없다() {
		DeliveryReadRequest request = new DeliveryReadRequest();
		LocalDateTime startDate = LocalDateTime.of(2025, 4, 1, 12, 0);
		LocalDateTime endDate = LocalDateTime.of(2025, 4, 5, 12, 0);
		request.setStartDate(startDate);
		request.setEndDate(endDate);

		assertThat(request.isValidDateRange()).isFalse();
	}

	@Test
	void 시작일이_종료일보다_미래인_경우_유효성_검사를_통과할_수_없다() {
		DeliveryReadRequest request = new DeliveryReadRequest();
		LocalDateTime startDate = LocalDateTime.of(2025, 4, 5, 12, 0);
		LocalDateTime endDate = LocalDateTime.of(2025, 4, 1, 12, 0);
		request.setStartDate(startDate);
		request.setEndDate(endDate);

		assertThat(request.isValidDateRange()).isFalse();
	}

	@Test
	void 날짜_차이가_정확히_3일이면_유효성_검사를_통과할_수_있다() {
		DeliveryReadRequest request = new DeliveryReadRequest();
		LocalDateTime startDate = LocalDateTime.of(2025, 4, 1, 12, 0);
		LocalDateTime endDate = LocalDateTime.of(2025, 4, 3, 12, 0);
		request.setStartDate(startDate);
		request.setEndDate(endDate);

		assertThat(request.isValidDateRange()).isTrue();
	}

	@Test
	void 두_날짜가_모두_설정된_경우_자동_설정이_적용되지_않을_수_있다() {
		DeliveryReadRequest request = new DeliveryReadRequest();
		LocalDateTime startDate = LocalDateTime.of(2025, 4, 1, 12, 0);
		LocalDateTime endDate = LocalDateTime.of(2025, 4, 1, 12, 0);
		request.setStartDate(startDate);
		request.setEndDate(endDate);

		LocalDateTime getStartDate = request.getStartDate();
		LocalDateTime getEndDate = request.getEndDate();

		assertThat(getStartDate).isEqualTo(startDate);
		assertThat(getEndDate).isEqualTo(endDate);
	}
}