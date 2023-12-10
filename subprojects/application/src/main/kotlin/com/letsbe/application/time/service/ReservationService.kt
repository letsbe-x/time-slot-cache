package com.letsbe.application.time.service

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.domain.time.aggregate.SavedReservationDo
import com.letsbe.domain.time.aggregate.UnsavedReservationDo
import com.letsbe.domain.time.repository.ReservationDoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ReservationService {
	@Autowired
	lateinit var reservationDoRepository: ReservationDoRepository

	@Autowired
	lateinit var fallbackReservationAvailableChecker: FallbackReservationAvailableChecker

	fun getReservation(reservationId: ReservationId): ReservationDo {
		return reservationDoRepository.getById(reservationId)
	}

	fun createReservation(interval: OpenEndRange<Instant>): ReservationDo? {
		if (fallbackReservationAvailableChecker.checkAvailable(interval).not()) {
			throw IllegalArgumentException("createReservation is not available")
		}

		val newReservationDo = UnsavedReservationDo(
			interval = interval
		)
		return reservationDoRepository.create(newReservationDo)
	}

	fun isAvailable(interval: OpenEndRange<Instant>): Boolean {
		return fallbackReservationAvailableChecker.checkAvailable(interval)
	}

	fun updateReservation(
		reservationId: ReservationId,
		interval: OpenEndRange<Instant>
	): SavedReservationDo? {
		if (fallbackReservationAvailableChecker.checkAvailable(interval).not()) {
			throw IllegalArgumentException("updateReservation is not available")
		}

		val savedReservationDo = reservationDoRepository.getById(reservationId)

		val updateReservationDo = ReservationDo(
			id = savedReservationDo.id,
			interval = interval
		)

		return reservationDoRepository.update(updateReservationDo)
	}

	fun deleteReservation(reservationId: ReservationId) {
		reservationDoRepository.deleteById(reservationId)
	}
}
