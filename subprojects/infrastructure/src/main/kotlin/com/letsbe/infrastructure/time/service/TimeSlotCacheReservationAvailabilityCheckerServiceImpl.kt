package com.letsbe.infrastructure.time.service

import com.letsbe.domain.time.service.ReservationAvailabilityCheckerService
import com.letsbe.infrastructure.time.cache.TimeSlotCacheRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class TimeSlotCacheReservationAvailabilityCheckerServiceImpl : ReservationAvailabilityCheckerService {

	@Autowired
	lateinit var timeSlotCacheRepository: TimeSlotCacheRepository

	override fun checkAvailable(interval: OpenEndRange<Instant>): Boolean {
		val baseTime = interval.start.truncatedTo(ChronoUnit.DAYS)
		val timeSlotCache = timeSlotCacheRepository.getTimeSlotCache(baseTime)
		return timeSlotCache.isAvailable(interval)
	}
}
