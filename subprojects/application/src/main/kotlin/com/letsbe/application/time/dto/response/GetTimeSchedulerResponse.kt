package com.letsbe.application.time.dto.response

import com.letsbe.domain.time.aggregate.ReservationDo

data class GetTimeSchedulerResponse(
	val reservationDo: List<ReservationDo>
)
