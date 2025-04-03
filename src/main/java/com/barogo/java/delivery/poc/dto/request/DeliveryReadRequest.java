package com.barogo.java.delivery.poc.dto.request;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DeliveryReadRequest {
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime startDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime endDate;

	public DeliveryReadRequest() {
	}

	public LocalDateTime getStartDate() {
		if (startDate == null) {
			if (endDate == null) {
				startDate = LocalDateTime.now().minusDays(3);
			} else {
				startDate = endDate.minusDays(3);
			}
		}
		return startDate;
	}

	public LocalDateTime getEndDate() {
		if (endDate == null) {
			if (startDate == null) {
				endDate = LocalDateTime.now();
			} else {
				endDate = startDate.plusDays(3);
			}
		}
		return endDate;
	}

	@AssertTrue(message = "조회 기간은 최대 3일까지만 가능합니다.")
	public boolean isValidDateRange() {
		LocalDateTime start = getStartDate();
		LocalDateTime end = getEndDate();

		if (end.isBefore(start)) {
			return false;
		}

		long daysBetween = ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());
		return daysBetween <= 3;
	}
}