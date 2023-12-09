package com.letsbe.domain.time.repository

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.ReservationId
import java.time.Instant

interface ReservationDoRepository {
	fun getById(reservationId: ReservationId): ReservationDo

	fun create(reservationDo: ReservationDo): ReservationDo
	fun deleteById(reservationId: ReservationId)
	fun update(reservationDo: ReservationDo): ReservationDo
	fun findByInterval(startAt: Instant, endAt: Instant): List<ReservationDo>
}
