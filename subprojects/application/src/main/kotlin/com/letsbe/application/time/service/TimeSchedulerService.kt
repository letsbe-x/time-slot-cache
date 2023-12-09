package com.letsbe.application.time.service

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.repository.TimeSchedulerDoRepository
import org.springframework.stereotype.Service
import java.util.Date

@Service
class TimeSchedulerService(
	private val timeSchedulerDoRepository: TimeSchedulerDoRepository
) {
	fun getTimeScheduler(date: Date): List<ReservationDo> {
		val timeSchedulerDo = timeSchedulerDoRepository.findByDate(date)

		return timeSchedulerDo.timeSchedule.map {
			it.value
		}
	}
}
