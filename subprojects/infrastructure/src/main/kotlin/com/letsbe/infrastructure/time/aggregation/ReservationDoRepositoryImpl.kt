package com.letsbe.infrastructure.time.aggregation

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.domain.time.repository.ReservationDoRepository
import com.letsbe.infrastructure.time.cache.TimeSlotCacheRepository
import com.letsbe.infrastructure.time.cache.impl.TimeSlotCacheRepositoryImpl.EnumCacheUpdateMethod
import com.letsbe.infrastructure.time.mapper.ReservationMapper
import com.letsbe.infrastructure.time.mapper.TimeSlotCacheMapper
import com.letsbe.infrastructure.time.repository.ReservationEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class ReservationDoRepositoryImpl : ReservationDoRepository {

	@Autowired
	private lateinit var reservationEntityRepository: ReservationEntityRepository

	@Autowired
	private lateinit var timeSlotCacheRepository: TimeSlotCacheRepository

	override fun getById(reservationId: ReservationId): ReservationDo {
		return reservationEntityRepository.findById(reservationId)
			.orElseThrow { NoSuchElementException("Reservation not found with id: $reservationId") }
			.run(ReservationMapper::toDo)
	}

	override fun findByInterval(startAt: Instant, endAt: Instant): List<ReservationDo> {
		val reservationEntityList = reservationEntityRepository.findReservationsOverlappingWithDay(startAt, endAt)

		return reservationEntityList.map(ReservationMapper::toDo)
	}

	override fun create(reservationDo: ReservationDo): ReservationDo {
		return reservationEntityRepository.save(ReservationMapper.toEntity(reservationDo))
			.run(ReservationMapper::toDo)
			.also {
				// TODO: update cache
				val intervalList = TimeSlotCacheMapper.splitByWeek(reservationDo.interval)

				intervalList.forEach { (baseTime, interval) ->
					timeSlotCacheRepository.updateTimeSlotCache(baseTime, interval, EnumCacheUpdateMethod.INSERT)
				}
			}
	}

	override fun deleteById(reservationId: ReservationId) {
		val baseReservation = reservationEntityRepository.findById(reservationId)
			.orElseThrow { NoSuchElementException("Reservation not found with id: $reservationId") }

		return reservationEntityRepository.deleteById(reservationId)
			.also {
				// TODO: update cache
				val intervalList = TimeSlotCacheMapper.splitByWeek(baseReservation.startAt..<baseReservation.endAt)

				intervalList.forEach { (baseTime, interval) ->
					timeSlotCacheRepository.updateTimeSlotCache(baseTime, interval, EnumCacheUpdateMethod.DELETE)
				}
			}
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
			.also {
				// TODO: update cache
				val intervalList = TimeSlotCacheMapper.splitByWeek(reservationDo.interval)

				intervalList.forEach { (baseTime, interval) ->
					timeSlotCacheRepository.updateTimeSlotCache(baseTime, interval, EnumCacheUpdateMethod.INSERT)
				}
			}
	}
}
