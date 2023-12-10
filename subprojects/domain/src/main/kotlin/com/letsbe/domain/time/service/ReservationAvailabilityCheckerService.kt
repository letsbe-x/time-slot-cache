package com.letsbe.domain.time.service

import java.time.Instant

interface ReservationAvailabilityCheckerService {
	fun checkAvailable(interval: OpenEndRange<Instant>): Boolean
}
