package com.letsbe.application.time.dto

import java.time.Instant

data class CreateReservationRequest(
	val startAt: Instant,
	val endAt: Instant
)
