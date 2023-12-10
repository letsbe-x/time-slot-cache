package com.letsbe.infrastructure.time.cache

import com.letsbe.domain.time.cache.ReservationCache
import java.time.Instant
interface ReservationCacheRepository {
	fun getReservationCache(startAt: Instant): ReservationCache

	fun setReservationCache(reservationCache: ReservationCache)
}
