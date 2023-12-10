package com.letsbe.domain.time.repository

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.domain.time.aggregate.SavedReservationDo
import com.letsbe.domain.time.aggregate.UnsavedReservationDo
import java.time.Instant

interface ReservationDoRepository {
	fun getById(reservationId: ReservationId): SavedReservationDo

	fun create(unsavedReservationDo: UnsavedReservationDo): SavedReservationDo
	fun deleteById(reservationId: ReservationId)
	fun update(reservationDo: ReservationDo): SavedReservationDo
	fun findByInterval(startAt: Instant, endAt: Instant): List<SavedReservationDo>
}
