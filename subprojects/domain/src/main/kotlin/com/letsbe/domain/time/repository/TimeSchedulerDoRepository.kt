package com.letsbe.domain.time.repository

import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.domain.time.aggregate.TimeSchedulerDo
import java.time.Instant
import java.util.Date

interface TimeSchedulerDoRepository {
	fun findByInterval(interval: OpenEndRange<Instant>): TimeSchedulerDo

	fun findByDate(date: Date, excludeReservationId: ReservationId? = null): TimeSchedulerDo
}
