package com.letsbe.application.time.dto.request

import java.time.Instant

data class CheckAvailableReservationRequest(
	val startAt: Instant,
	val endAt: Instant
)
