package com.letsbe.domain.time.aggregate

import java.time.Instant
import java.time.temporal.ChronoUnit

typealias ReservationId = Long

data class ReservationDo(
	val id: ReservationId? = null,
	val interval: OpenEndRange<Instant>
) {

	companion object {
		fun create(id: ReservationId, interval: OpenEndRange<Instant>): ReservationDo {
			isValid(interval)
			return ReservationDo(
				id = id,
				interval = interval
			)
		}

		private fun isValid(interval: OpenEndRange<Instant>) {
			val startDate = interval.start.truncatedTo(ChronoUnit.DAYS)
			val endDate = interval.endExclusive.truncatedTo(ChronoUnit.DAYS)

			if (startDate != endDate && startDate.plus(1, ChronoUnit.DAYS) != endDate) {
				TODO("현재는 동일 날짜의 예약만 가능합니다. 스펙 확장 필요")
			}
		}
	}
}
