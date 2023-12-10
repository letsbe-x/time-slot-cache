package com.letsbe.application.time.service

import com.letsbe.domain.time.service.ReservationAvailabilityCheckerService
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class FallbackReservationAvailabilityChecker(
	private val availabilityChecker: List<ReservationAvailabilityCheckerService>
) {
	fun checkAvailable(interval: OpenEndRange<Instant>): Boolean {
		availabilityChecker.forEach {
			try {
				return it.checkAvailable(interval)
			} catch (_: Exception) {
			}
		}
		throw IllegalArgumentException("No availability checker")
	}
}
