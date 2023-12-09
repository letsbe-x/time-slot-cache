package com.letsbe.domain.time.aggregate

import org.springframework.data.domain.Range
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.TreeMap

data class TimeSchedulerDo(
	val date: Date,
	val timeSchedule: TreeMap<Instant, ReservationDo>
) {

	fun isAvailable(interval: OpenEndRange<Instant>): Boolean {
		if (isValid(interval).not()) return false
		if (timeSchedule.isEmpty()) return true

		/**
		 * |---------<           |---------<
		 *       |----------------<
		 *     startAt		   endAt
		 */
		// interval.start 이전에 시작하는 예약 중 가장 늦게 끝나는 예약 찾기
		val beforeInterval = timeSchedule.floorEntry(interval.start)
		if (beforeInterval != null && beforeInterval.value.interval.endExclusive.isAfter(interval.start)) {
			return false
		}

		// interval.endExclusive 이후에 시작하는 예약 중 가장 빨리 시작하는 예약 찾기
		// 여기서는 interval.endExclusive 이전에 시작하는 예약을 찾아야 합니다.
		val afterInterval = timeSchedule.lowerEntry(interval.endExclusive)
		if (afterInterval != null && afterInterval.value.interval.endExclusive.isAfter(interval.start)) {
			return false
		}

		return true
	}

	private val availableDateInterval: Range<Instant> by lazy { Range.closed(date.toInstant(), date.toInstant().plus(1, ChronoUnit.DAYS)) }

	private fun isValid(interval: OpenEndRange<Instant>): Boolean {
		return interval.start in availableDateInterval && interval.endExclusive in availableDateInterval
	}

	companion object {
		fun create(reservationDoList: List<ReservationDo>): TimeSchedulerDo {
			isValid(reservationDoList.map { it.interval })

			val date = Date.from(reservationDoList.first().interval.start)
			val timeSchedule = TreeMap<Instant, ReservationDo>()
			reservationDoList.forEach {
				timeSchedule[it.interval.start] = it
			}
			return TimeSchedulerDo(date, timeSchedule)
		}

		private fun isValid(intervalList: List<OpenEndRange<Instant>>): Boolean {
			if (intervalList.isEmpty()) return true
			// 모두 같은 date인지 확인
			val date = intervalList.first().start.truncatedTo(ChronoUnit.DAYS)
			if (intervalList.any { it.start.truncatedTo(ChronoUnit.DAYS) != date }) return false
			if (intervalList.any { it.endExclusive.truncatedTo(ChronoUnit.DAYS) != date || it.endExclusive == date.plus(1, ChronoUnit.DAYS) }) return false
			return true
		}
	}
}
