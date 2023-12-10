package com.letsbe.domain.time.aggregate

import com.letsbe.domain.annotation.AllOpen
import java.time.Instant

typealias ReservationId = Long

data class ReservationDo(
	val id: ReservationId?,
	override val interval: OpenEndRange<Instant>
) : UnsavedReservationDo(interval)

@AllOpen
class UnsavedReservationDo(
	val interval: OpenEndRange<Instant>
) {
	fun toDo(): ReservationDo {
		return ReservationDo(
			id = null,
			interval = interval
		)
	}
	companion object {
		fun create(interval: OpenEndRange<Instant>): UnsavedReservationDo {
			return UnsavedReservationDo(
				interval = interval
			)
		}
	}
}

class SavedReservationDo(
	override val id: ReservationId,
	override val interval: OpenEndRange<Instant>
) : ReservationDo(id, interval) {

	companion object {
		fun create(id: ReservationId, interval: OpenEndRange<Instant>): SavedReservationDo {
			return SavedReservationDo(
				id = id,
				interval = interval
			)
		}
	}
}
