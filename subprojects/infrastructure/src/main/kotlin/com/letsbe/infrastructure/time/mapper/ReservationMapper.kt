package com.letsbe.infrastructure.time.mapper

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.infrastructure.time.entity.ReservationEntity

object ReservationMapper {
	fun toDo(reservationEntity: ReservationEntity): ReservationDo {
		return ReservationDo.create(
			id = reservationEntity.id!!,
			interval = reservationEntity.startAt ..< reservationEntity.endAt
		)
	}

	fun toEntity(reservationDo: ReservationDo): ReservationEntity {
		return ReservationEntity(
			id = reservationDo.id,
			startAt = reservationDo.interval.start,
			endAt = reservationDo.interval.endExclusive
		)
	}
}
