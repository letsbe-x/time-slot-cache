package com.letsbe.domain.time.cache

import com.letsbe.domain.time.aggregate.ReservationId
import java.time.Instant

data class ReservationCache(
	val startAt: Instant,
	val reservationId: ReservationId
)
