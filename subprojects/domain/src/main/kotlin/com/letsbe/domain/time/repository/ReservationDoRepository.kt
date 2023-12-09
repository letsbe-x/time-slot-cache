package com.letsbe.domain.time.repository

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.ReservationId

interface ReservationDoRepository {
	fun getById(reservationId: ReservationId): ReservationDo

	fun create(reservation: ReservationDo): ReservationDo
	fun deleteById(reservationId: ReservationId)
	fun update(reservationDo: ReservationDo): ReservationDo
}
