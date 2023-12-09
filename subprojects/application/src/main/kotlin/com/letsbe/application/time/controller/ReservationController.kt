package com.letsbe.application.time.controller

import com.letsbe.application.time.dto.request.CreateReservationRequest
import com.letsbe.application.time.dto.request.UpdateReservationRequest
import com.letsbe.application.time.service.ReservationService
import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.ReservationId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ReservationController(
	private val reservationService: ReservationService
) {

	@GetMapping("/reservation/{reservationId}")
	fun getReservation(
		@PathVariable reservationId: ReservationId
	): ResponseEntity<ReservationDo> {
		return ResponseEntity.ok(
			reservationService.getReservation(reservationId)
		)
	}

	@PostMapping("/reservation")
	fun createReservation(
		@RequestBody createReservationRequest: CreateReservationRequest
	): ResponseEntity<ReservationDo> {
		return ResponseEntity.ok(
			reservationService.createReservation(createReservationRequest)
		)
	}

	@PatchMapping("/reservation/{reservationId}")
	fun updateReservation(
		@PathVariable reservationId: ReservationId,
		@RequestBody updateReservationRequest: UpdateReservationRequest
	): ResponseEntity<ReservationDo> {
		return ResponseEntity.ok(
			reservationService.updateReservation(reservationId, updateReservationRequest)
		)
	}

	@DeleteMapping("/reservation/{reservationId}")
	fun deleteReservation(
		@PathVariable reservationId: ReservationId
	): ResponseEntity<*> {
		reservationService.deleteReservation(reservationId)
		return ResponseEntity.ok().build<Any>()
	}
}
