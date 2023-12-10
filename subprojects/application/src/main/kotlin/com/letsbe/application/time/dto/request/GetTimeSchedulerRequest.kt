package com.letsbe.application.time.dto.request

import java.time.Instant

data class GetTimeSchedulerRequest(
	val startAt: Instant,
	val endAt: Instant
)
