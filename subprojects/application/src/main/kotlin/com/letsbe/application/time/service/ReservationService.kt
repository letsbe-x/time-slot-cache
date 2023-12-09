package com.letsbe.application.time.service

import com.letsbe.application.time.dto.request.CreateReservationRequest
import com.letsbe.application.time.dto.request.UpdateReservationRequest
import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.domain.time.repository.ReservationDoRepository
import com.letsbe.domain.time.repository.TimeSchedulerDoRepository
import org.springframework.stereotype.Service

@Service
class ReservationService(
	private val reservationDoRepository: ReservationDoRepository,
	private val timeSchedulerDoRepository: TimeSchedulerDoRepository
) {
	fun getReservation(reservationId: ReservationId): ReservationDo {
		return reservationDoRepository.getById(reservationId)
	}

	fun createReservation(createReservationRequest: CreateReservationRequest): ReservationDo? {
		val newReservationDo = ReservationDo(
			interval = with(createReservationRequest) {
				startAt ..< endAt
			}
		)
		return reservationDoRepository.create(newReservationDo)
	}

	fun updateReservation(reservationId: ReservationId, updateReservationRequest: UpdateReservationRequest): ReservationDo? {
		val reservationDo = reservationDoRepository.getById(reservationId) ?: throw NoSuchElementException("Reservation not found with id: $reservationId")

		val newReservationDo = ReservationDo(
			id = reservationDo.id,
			interval = with(updateReservationRequest) {
				startAt ..< endAt
			}
		)

		return reservationDoRepository.create(newReservationDo)
	}

	fun deleteReservation(reservationId: ReservationId) {
		reservationDoRepository.deleteById(reservationId)
	}
}
