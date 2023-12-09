package com.letsbe.application.time.dto.request

import org.springframework.format.annotation.DateTimeFormat
import java.util.Date

data class GetTimeSchedulerRequest(
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	val date: Date
)
