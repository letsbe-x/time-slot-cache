package com.letsbe.infrastructure.time.cache.impl

import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.domain.time.cache.ReservationCache
import java.time.Instant

object ReservationCacheMapper {
	fun toReservationCache(startAt: Instant, reservationId: ReservationId): ReservationCache {
		return ReservationCache(
			startAt = startAt,
			reservationId = reservationId
		)
	}
}
