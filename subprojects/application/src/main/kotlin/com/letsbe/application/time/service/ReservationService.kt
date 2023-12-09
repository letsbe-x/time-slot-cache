package com.letsbe.application.time.service

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.domain.time.repository.ReservationDoRepository
import com.letsbe.domain.time.repository.TimeSchedulerDoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

@Service
class ReservationService {
	@Autowired
	lateinit var reservationDoRepository: ReservationDoRepository

	@Autowired
	lateinit var timeSchedulerDoRepository: TimeSchedulerDoRepository

	fun getReservation(reservationId: ReservationId): ReservationDo {
		return reservationDoRepository.getById(reservationId)
	}

	fun createReservation(interval: OpenEndRange<Instant>): ReservationDo? {
		// TODO: 현재는 동일 시간의 예약 가능합니다. 스펙 확장 필요
		val timeSchedulerDo = timeSchedulerDoRepository.findByDate(Date.from(interval.start))
		if (timeSchedulerDo.isAvailable(interval).not()) {
			throw IllegalArgumentException("createReservation is not available")
		}

		// TODO: id가 없는 경우는 create, 있는 경우는 update 구분필요
		val newReservationDo = ReservationDo(
			interval = interval
		)
		return reservationDoRepository.create(newReservationDo)
	}

	fun updateReservation(reservationId: ReservationId, interval: OpenEndRange<Instant>): ReservationDo? {
		// TODO: 현재는 동일 시간의 예약 가능합니다. 스펙 확장 필요
		val timeSchedulerDo = timeSchedulerDoRepository.findByDate(Date.from(interval.start), excludeReservationId = reservationId)
		if (timeSchedulerDo.isAvailable(interval).not()) {
			throw IllegalArgumentException("updateReservation is not available")
		}

		val reservationDo = reservationDoRepository.getById(reservationId)

		// TODO: id가 없는 경우는 create, 있는 경우는 update 구분필요
		val updateReservationDo = ReservationDo(
			id = reservationDo.id,
			interval = interval
		)

		return reservationDoRepository.update(updateReservationDo)
	}

	fun deleteReservation(reservationId: ReservationId) {
		reservationDoRepository.deleteById(reservationId)
	}
}
