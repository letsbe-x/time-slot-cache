package com.letsbe.infrastructure.time.service

import com.letsbe.domain.time.repository.TimeSchedulerDoRepository
import com.letsbe.domain.time.service.ReservationAvailabilityCheckerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class TimeSchedulerReservationAvailabilityCheckerServiceImpl : ReservationAvailabilityCheckerService {

	@Autowired
	lateinit var timeSchedulerDoRepository: TimeSchedulerDoRepository

	override fun checkAvailable(interval: OpenEndRange<Instant>): Boolean {
		val timeSchedulerDo = timeSchedulerDoRepository.findByInterval(interval)
		return timeSchedulerDo.isAvailable(interval)
	}
}
