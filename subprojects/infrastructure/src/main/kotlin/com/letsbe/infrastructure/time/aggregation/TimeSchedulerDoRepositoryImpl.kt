package com.letsbe.infrastructure.time.aggregation

import com.letsbe.domain.time.aggregate.ReservationId
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

	override fun findByInterval(interval: OpenEndRange<Instant>, excludeReservationId: ReservationId?): TimeSchedulerDo {
		val reservationDoList = reservationDoRepository.findByInterval(interval.start, interval.endExclusive)
		val timeSchedule = TreeMap<Instant, OpenEndRange<Instant>>()
		reservationDoList
			.filter { it.id != excludeReservationId }
			.map {
				timeSchedule[it.interval.start] = it.interval
			}
		return TimeSchedulerDo(timeSchedule)
	}

	override fun findByDate(date: Date, excludeReservationId: ReservationId?): TimeSchedulerDo {
		val dayStart = date.toInstant()
		val dayEnd = dayStart.plus(1, ChronoUnit.DAYS)

		val reservationDoList = reservationDoRepository.findByInterval(dayStart, dayEnd)

		val timeSchedule = TreeMap<Instant, OpenEndRange<Instant>>()
		reservationDoList
			.filter { it.id != excludeReservationId }
			.forEach { timeSchedule[it.interval.start] = it.interval }

		return TimeSchedulerDo(timeSchedule)
	}
}
