package com.letsbe.infrastructure.time.cache.impl

import com.letsbe.domain.time.cache.ReservationCache
import com.letsbe.infrastructure.time.cache.ReservationCacheRepository
import io.lettuce.core.RedisClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class ReservationCacheRepositoryImpl : ReservationCacheRepository {
	@Autowired
	lateinit var redisClient: RedisClient

	override fun getReservationCache(startAt: Instant): ReservationCache {
		val connection = redisClient.connect()
		val syncCommands = connection.sync()
		val reservationId = syncCommands.get(toKey(Instant.now()))
			?: throw IllegalArgumentException("ReservationCache not found")
		return ReservationCache(
			startAt = startAt,
			reservationId = reservationId.toLong()
		)
	}

	override fun setReservationCache(reservationCache: ReservationCache) {
		val connection = redisClient.connect()
		val syncCommands = connection.sync()
		syncCommands.set(toKey(reservationCache.startAt), reservationCache.reservationId.toString())
	}

	private fun toKey(startAt: Instant): String = "reservation:${startAt.toEpochMilli()}"
}
