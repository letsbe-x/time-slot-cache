package com.letsbe.infrastructure.time.aggregation

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.TimeSchedulerDo
import com.letsbe.domain.time.repository.ReservationDoRepository
import com.letsbe.domain.time.repository.TimeSchedulerDoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.TreeMap

@Component
class TimeSchedulerDoRepositoryImpl : TimeSchedulerDoRepository {

	@Autowired
	private lateinit var reservationDoRepository: ReservationDoRepository

	override fun findByInterval(startAt: Instant, endAt: Instant): List<TimeSchedulerDo> {
		val dayStart = startAt.truncatedTo(ChronoUnit.DAYS)
		val dayEnd = endAt.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS)

		val reservationDoList = reservationDoRepository.findByInterval(dayStart, dayEnd)
		return reservationDoList
			.groupBy { it.interval.start.truncatedTo(ChronoUnit.DAYS) }
			.map { (startAt, reservationDoList) ->
				val date = Date.from(startAt)
				val timeSchedule = TreeMap<Instant, ReservationDo>()
				reservationDoList.forEach {
					timeSchedule[it.interval.start] = it
				}
				TimeSchedulerDo(date, timeSchedule)
			}
	}

	override fun findByDate(date: Date): TimeSchedulerDo {
		val dayStart = date.toInstant()
		val dayEnd = dayStart.plus(1, ChronoUnit.DAYS)

		val reservationDoList = reservationDoRepository.findByInterval(dayStart, dayEnd)

		val timeSchedule = TreeMap<Instant, ReservationDo>()
		reservationDoList.forEach {
			timeSchedule[it.interval.start] = it
		}

		return TimeSchedulerDo(date, timeSchedule)
	}
}
