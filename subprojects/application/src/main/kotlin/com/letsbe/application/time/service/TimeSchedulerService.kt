package com.letsbe.application.time.service

import com.letsbe.domain.time.repository.TimeSchedulerDoRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TimeSchedulerService(
	private val timeSchedulerDoRepository: TimeSchedulerDoRepository
) {
	fun getTimeScheduler(interval: OpenEndRange<Instant>): List<OpenEndRange<Instant>> {
		val timeSchedulerDo = timeSchedulerDoRepository.findByInterval(interval)

		return timeSchedulerDo.timeSchedule.map {
			it.value
		}
	}
}
