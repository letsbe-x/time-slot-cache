package com.letsbe.domain.time.aggregate

import java.time.Instant

typealias ReservationId = Long

data class ReservationDo(
	val id: ReservationId? = null,
	val interval: OpenEndRange<Instant>
)
