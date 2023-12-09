package com.letsbe.application.time.service

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.repository.ReservationDoRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Instant

class ReservationServiceTest {

	private lateinit var reservationService: ReservationService
	private val reservationDoRepository: ReservationDoRepository = mock()

	@BeforeEach
	fun setUp() {
		reservationService = ReservationService(reservationDoRepository)
	}

	@Test
	fun `getReservation should return reservation when it exists`() {
		val reservationId = 1L
		val expectedReservation = ReservationDo(
			id = reservationId,
			interval = Instant.now()..<Instant.now().plusSeconds(3600)
		)
		`when`(reservationDoRepository.getById(reservationId)).thenReturn(expectedReservation)

		val result = reservationService.getReservation(reservationId)

		assertEquals(expectedReservation, result)
	}
}
