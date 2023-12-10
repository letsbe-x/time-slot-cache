package com.letsbe.application.time.dto.response

import java.time.Instant

data class GetTimeSchedulerResponse(
	val reservationDo: List<OpenEndRange<Instant>>
)
