package com.letsbe.application.time.controller

import com.letsbe.application.time.dto.request.GetTimeSchedulerRequest
import com.letsbe.application.time.dto.response.GetTimeSchedulerResponse
import com.letsbe.application.time.service.TimeSchedulerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TimeScheduleController(
	private val timeSchedulerService: TimeSchedulerService
) {
	@GetMapping("/time-schedule")
	fun getTimeScheduler(
		getTimeSchedulerRequest: GetTimeSchedulerRequest
	): ResponseEntity<GetTimeSchedulerResponse> {
		val interval = with(getTimeSchedulerRequest) {
			startAt ..< endAt
		}
		return ResponseEntity.ok(
			GetTimeSchedulerResponse(
				timeSchedulerService.getTimeScheduler(interval)
			)
		)
	}
}
