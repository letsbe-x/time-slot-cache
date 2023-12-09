package com.letsbe.domain.time.aggregate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.TreeMap

class TimeSchedulerDoTest {

	@Test
	fun `isAvailable should return true when the time schedule is empty`() {
		val date = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS))
		val timeSchedulerDo = TimeSchedulerDo(date, TreeMap())
		val interval = date.toInstant() ..< date.toInstant().plus(1, ChronoUnit.DAYS)

		val result = timeSchedulerDo.isAvailable(interval)

		assertThat(result).isTrue()
	}

	@Test
	fun `isAvailable should return false when the interval is not within the date range`() {
		val date = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS))
		val timeSchedulerDo = TimeSchedulerDo(date, TreeMap())
		val beforeInterval = date.toInstant().minus(1, ChronoUnit.MINUTES) ..< date.toInstant().plus(1, ChronoUnit.DAYS)
		val result1 = timeSchedulerDo.isAvailable(beforeInterval)
		assertThat(result1).isFalse()

		val afterInterval = date.toInstant() ..< date.toInstant().plus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.MINUTES)
		val result2 = timeSchedulerDo.isAvailable(afterInterval)
		assertThat(result2).isFalse()
	}

	@Test
	fun `isAvailable should return false when the interval overlaps with existing reservations`() {
		val date = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS))
		val dateStartAt = date.toInstant()
		val timeSchedulerDo = TimeSchedulerDo.create(
			listOf(
				// 00:00 ~ 01:00
				createReservationDo(1, dateStartAt.plus(0, ChronoUnit.HOURS), dateStartAt.plus(1, ChronoUnit.HOURS)),
				// 02:00 ~ 04:00
				createReservationDo(2, dateStartAt.plus(2, ChronoUnit.HOURS), dateStartAt.plus(4, ChronoUnit.HOURS)),
				// 06:00 ~ 10:00
				createReservationDo(3, dateStartAt.plus(6, ChronoUnit.HOURS), dateStartAt.plus(10, ChronoUnit.HOURS)),
				// 22:00 ~ 24:00
				createReservationDo(4, dateStartAt.plus(22, ChronoUnit.HOURS), dateStartAt.plus(24, ChronoUnit.HOURS))
			)
		)

		// 겹치는 경우
		// 00:00 ~ 01:00
		val overlapInterval1 = dateStartAt.plus(0, ChronoUnit.HOURS) ..< dateStartAt.plus(1, ChronoUnit.HOURS)
		assertThat(timeSchedulerDo.isAvailable(overlapInterval1)).isFalse()
		// 00:30 ~ 01:30
		val overlapInterval2 = dateStartAt.plus(0, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES) ..< dateStartAt.plus(1, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES)
		assertThat(timeSchedulerDo.isAvailable(overlapInterval2)).isFalse()
		// 20:00 ~ 24:00
		val overlapInterval3 = dateStartAt.plus(20, ChronoUnit.HOURS) ..< dateStartAt.plus(24, ChronoUnit.HOURS)
		assertThat(timeSchedulerDo.isAvailable(overlapInterval3)).isFalse()

		// 겹치지 않는 경우
		// 01:00 ~ 02:00
		val availableInterval1 = dateStartAt.plus(1, ChronoUnit.HOURS) ..< dateStartAt.plus(2, ChronoUnit.HOURS)
		assertThat(timeSchedulerDo.isAvailable(availableInterval1)).isTrue()
		// 04:00 ~ 06:00
		val availableInterval2 = dateStartAt.plus(4, ChronoUnit.HOURS) ..< dateStartAt.plus(6, ChronoUnit.HOURS)
		assertThat(timeSchedulerDo.isAvailable(availableInterval2)).isTrue()
		// 10:00 ~ 22:00
		val availableInterval3 = dateStartAt.plus(10, ChronoUnit.HOURS) ..< dateStartAt.plus(22, ChronoUnit.HOURS)
		assertThat(timeSchedulerDo.isAvailable(availableInterval3)).isTrue()
	}

	private fun createReservationDo(id: ReservationId, startAt: Instant, endAt: Instant): ReservationDo {
		return ReservationDo(id, startAt ..< endAt)
	}
}
