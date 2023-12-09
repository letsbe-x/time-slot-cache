package com.letsbe.application.time.dto

import java.time.Instant

data class UpdateReservationRequest(
	val startAt: Instant,
	val endAt: Instant
)
