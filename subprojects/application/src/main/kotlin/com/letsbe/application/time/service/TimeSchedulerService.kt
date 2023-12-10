package com.letsbe.application.time.service

import com.letsbe.domain.time.repository.TimeSchedulerDoRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

@Service
class TimeSchedulerService(
	private val timeSchedulerDoRepository: TimeSchedulerDoRepository
) {
	fun getTimeScheduler(date: Date): List<OpenEndRange<Instant>> {
		val timeSchedulerDo = timeSchedulerDoRepository.findByDate(date)

		return timeSchedulerDo.timeSchedule.map {
			it.value
		}
	}
}
