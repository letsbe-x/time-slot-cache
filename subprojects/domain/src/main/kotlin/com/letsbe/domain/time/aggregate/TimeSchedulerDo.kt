package com.letsbe.domain.time.aggregate

import java.time.Instant
import java.util.TreeMap

data class TimeSchedulerDo(
	val timeSchedule: TreeMap<Instant, OpenEndRange<Instant>>
) {

	fun isAvailable(interval: OpenEndRange<Instant>): Boolean {
		if (timeSchedule.isEmpty()) return true

		/**
		 * |---------<           |---------<
		 *       |----------------<
		 *     startAt		   endAt
		 */
		// interval.start 이전에 시작하는 예약 중 가장 늦게 끝나는 예약 찾기
		val beforeInterval = timeSchedule.floorEntry(interval.start)
		if (beforeInterval != null && beforeInterval.value.endExclusive.isAfter(interval.start)) {
			return false
		}

		// interval.endExclusive 이후에 시작하는 예약 중 가장 빨리 시작하는 예약 찾기
		// 여기서는 interval.endExclusive 이전에 시작하는 예약을 찾아야 합니다.
		val afterInterval = timeSchedule.lowerEntry(interval.endExclusive)
		if (afterInterval != null && afterInterval.value.endExclusive.isAfter(interval.start)) {
			return false
		}

		return true
	}

	companion object {
		fun create(reservationDoList: List<ReservationDo>): TimeSchedulerDo {
			val timeSchedule = TreeMap<Instant, OpenEndRange<Instant>>()
			reservationDoList.forEach {
				timeSchedule[it.interval.start] = it.interval
			}
			return TimeSchedulerDo(timeSchedule)
		}
	}
}
