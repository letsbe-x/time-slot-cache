package com.letsbe.infrastructure.time.aggregation

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.domain.time.repository.ReservationDoRepository
import com.letsbe.infrastructure.time.mapper.ReservationMapper
import com.letsbe.infrastructure.time.repository.ReservationEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ReservationDoRepositoryImpl : ReservationDoRepository {

	@Autowired
	private lateinit var reservationEntityRepository: ReservationEntityRepository

	override fun getById(reservationId: ReservationId): ReservationDo {
		return reservationEntityRepository.findById(reservationId)
			.orElseThrow { NoSuchElementException("Reservation not found with id: $reservationId") }
			.run(ReservationMapper::toDo)
	}

	override fun create(reservation: ReservationDo): ReservationDo {
		return reservationEntityRepository.save(ReservationMapper.toEntity(reservation))
			.run(ReservationMapper::toDo)
	}

	override fun deleteById(reservationId: ReservationId) {
		reservationEntityRepository.findById(reservationId)
			.orElseThrow { NoSuchElementException("Reservation not found with id: $reservationId") }

		return reservationEntityRepository.deleteById(reservationId)
	}

	override fun update(reservationDo: ReservationDo): ReservationDo {
		val reservationId = reservationDo.id

		val reservationEntity = reservationEntityRepository.findById(reservationId!!)
			.orElseThrow { NoSuchElementException("Reservation not found with id: $reservationId") }

		with(reservationEntity) {
			startAt = reservationDo.interval.start
			endAt = reservationDo.interval.endExclusive
		}

		return reservationEntityRepository.save(reservationEntity)
			.run(ReservationMapper::toDo)
	}
}
