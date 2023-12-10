package com.letsbe.application.time.service

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.domain.time.aggregate.SavedReservationDo
import com.letsbe.domain.time.aggregate.UnsavedReservationDo
import com.letsbe.domain.time.repository.ReservationDoRepository
import com.letsbe.domain.time.repository.TimeSchedulerDoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ReservationService {
	@Autowired
	lateinit var reservationDoRepository: ReservationDoRepository

	@Autowired
	lateinit var timeSchedulerDoRepository: TimeSchedulerDoRepository

	@Autowired
	lateinit var fallbackReservationAvailabilityChecker: FallbackReservationAvailabilityChecker

	fun getReservation(reservationId: ReservationId): ReservationDo {
		return reservationDoRepository.getById(reservationId)
	}

	fun createReservation(interval: OpenEndRange<Instant>): ReservationDo? {
		val timeSchedulerDo = timeSchedulerDoRepository.findByInterval(interval)
		if (timeSchedulerDo.isAvailable(interval).not()) {
			throw IllegalArgumentException("createReservation is not available")
		}

		val newReservationDo = UnsavedReservationDo(
			interval = interval
		)
		return reservationDoRepository.create(newReservationDo)
	}

	fun isAvailable(interval: OpenEndRange<Instant>): Boolean {
		val timeSchedulerDo = timeSchedulerDoRepository.findByInterval(interval)
		return timeSchedulerDo.isAvailable(interval).not()
	}

	fun updateReservation(
		reservationId: ReservationId,
		interval: OpenEndRange<Instant>
	): SavedReservationDo? {
		val timeSchedulerDo = timeSchedulerDoRepository.findByInterval(interval, excludeReservationId = reservationId)
		if (timeSchedulerDo.isAvailable(interval).not()) {
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
