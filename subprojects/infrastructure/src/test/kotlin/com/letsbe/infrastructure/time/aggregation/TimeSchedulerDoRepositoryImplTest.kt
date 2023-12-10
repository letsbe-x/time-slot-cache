package com.letsbe.infrastructure.time.aggregation

import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.domain.time.aggregate.SavedReservationDo
import com.letsbe.domain.time.repository.ReservationDoRepository
import com.letsbe.infrastructure.AbstractRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class TimeSchedulerDoRepositoryImplTest : AbstractRepositoryTest() {

	@Mock
	private lateinit var reservationDoRepository: ReservationDoRepository

	@InjectMocks
	private lateinit var timeSchedulerDoRepository: TimeSchedulerDoRepositoryImpl

	@Test
	fun `findByInterval should group reservations by day and return TimeSchedulerDo list`() {
		val startAt = Instant.parse("2023-12-10T00:00:00Z")
		val endAt = startAt.plus(1, ChronoUnit.DAYS)
		val interval = startAt..<endAt

		val reservationDo1 = createReservationDo(1, startAt.plus(1, ChronoUnit.HOURS), startAt.plus(2, ChronoUnit.HOURS))
		val reservationDo2 = createReservationDo(2, startAt.plus(3, ChronoUnit.HOURS), startAt.plus(4, ChronoUnit.HOURS))

		`when`(reservationDoRepository.findByInterval(startAt, endAt))
			.thenReturn(listOf(reservationDo1, reservationDo2))

		val timeSchedulerDo = timeSchedulerDoRepository.findByInterval(interval)

		assertThat(timeSchedulerDo.timeSchedule.size).isEqualTo(2)
		assertThat(timeSchedulerDo.timeSchedule[reservationDo1.interval.start]).isEqualTo(reservationDo1.interval)
		assertThat(timeSchedulerDo.timeSchedule[reservationDo2.interval.start]).isEqualTo(reservationDo2.interval)
	}

	@Test
	fun `findByDate should group reservations by day and return TimeSchedulerDo`() {
		val startAt = Instant.parse("2023-12-11T00:00:00Z")
		val date = Date.from(startAt)
		val dayStart = startAt
		val dayEnd = dayStart.plus(1, ChronoUnit.DAYS)

		val reservationDo1 = createReservationDo(1, dayStart.plus(1, ChronoUnit.HOURS), dayStart.plus(2, ChronoUnit.HOURS))
		val reservationDo2 = createReservationDo(2, dayStart.plus(3, ChronoUnit.HOURS), dayStart.plus(4, ChronoUnit.HOURS))

		`when`(reservationDoRepository.findByInterval(dayStart, dayEnd))
			.thenReturn(listOf(reservationDo1, reservationDo2))

		val timeSchedulerDo = timeSchedulerDoRepository.findByDate(date)

		assertThat(timeSchedulerDo.timeSchedule.size).isEqualTo(2)
		assertThat(timeSchedulerDo.timeSchedule[reservationDo1.interval.start]).isEqualTo(reservationDo1.interval)
		assertThat(timeSchedulerDo.timeSchedule[reservationDo2.interval.start]).isEqualTo(reservationDo2.interval)
	}

	private fun createReservationDo(id: ReservationId, startAt: Instant, endAt: Instant): SavedReservationDo {
		return SavedReservationDo(id, startAt..<endAt)
	}
}
