package com.letsbe.infrastructure.time.service

import com.letsbe.domain.time.service.ReservationAvailabilityCheckerService
import com.letsbe.infrastructure.time.cache.TimeSlotCacheRepository
import com.letsbe.infrastructure.time.mapper.TimeSlotCacheMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class TimeSlotCacheReservationAvailabilityCheckerServiceImpl : ReservationAvailabilityCheckerService {

	@Autowired
	lateinit var timeSlotCacheRepository: TimeSlotCacheRepository

	override fun checkAvailable(interval: OpenEndRange<Instant>): Boolean {
		val intervalList = TimeSlotCacheMapper.splitByWeek(interval)

		return intervalList.all { (baseTime, interval) ->
			timeSlotCacheRepository.getTimeSlotCache(baseTime).isAvailable(interval)
		}
	}
}
