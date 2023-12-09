package com.letsbe.application.time.dto.request

import java.time.Instant

data class UpdateReservationRequest(
	val startAt: Instant,
	val endAt: Instant
)
