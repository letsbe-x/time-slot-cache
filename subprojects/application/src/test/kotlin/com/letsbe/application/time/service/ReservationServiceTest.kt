package com.letsbe.application.time.service

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.domain.time.aggregate.SavedReservationDo
import com.letsbe.domain.time.aggregate.UnsavedReservationDo
import com.letsbe.domain.time.repository.ReservationDoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.time.temporal.ChronoUnit

@ExtendWith(MockitoExtension::class)
class ReservationServiceTest {

	@Mock
	private lateinit var reservationDoRepository: ReservationDoRepository

	@Mock
	private lateinit var fallbackReservationAvailableChecker: FallbackReservationAvailableChecker

	@InjectMocks
	private lateinit var reservationService: ReservationService

	@Test
	fun `getReservation should call reservationDoRepository_getById`() {
		val reservationId = 1L
		
		val expectedDo = mock<SavedReservationDo>()
		`when`(reservationDoRepository.getById(reservationId)).thenReturn(expectedDo)

		val actualDo = reservationService.getReservation(reservationId)

		verify(reservationDoRepository, times(1)).getById(reservationId)
		assertThat(actualDo).isEqualTo(expectedDo)
	}

	@Test
	fun `createReservation should call timeSchedulerDoRepository_findByDate and throw if not available`() {
		val startInstant = Instant.now()
		val interval = startInstant ..< startInstant.plus(1, ChronoUnit.HOURS)

		`when`(fallbackReservationAvailableChecker.checkAvailable(interval)).thenReturn(false)

		assertThrows<IllegalArgumentException> {
			reservationService.createReservation(interval)
		}

		verifyNoMoreInteractions(reservationDoRepository)
	}

	@Test
	fun `createReservation should call reservationDoRepository_create and return new ReservationDo`() {
		val startInstant = Instant.now()
		val interval = startInstant ..< startInstant.plus(1, ChronoUnit.HOURS)

		`when`(fallbackReservationAvailableChecker.checkAvailable(interval)).thenReturn(true)

		val expectedDo = mock<SavedReservationDo>()
		`when`(reservationDoRepository.create(any(UnsavedReservationDo::class.java))).thenReturn(expectedDo)

		reservationService.createReservation(interval)

		verify(reservationDoRepository, times(1)).create(any(UnsavedReservationDo::class.java))
	}

	@Test
	fun `updateReservation should call timeSchedulerDoRepository_findByDate with excludeReservationId and throw if not available`() {
		val reservationId = 1L
		val startInstant = Instant.now()
		val interval = startInstant ..< startInstant.plus(1, ChronoUnit.HOURS)

		`when`(fallbackReservationAvailableChecker.checkAvailable(interval)).thenReturn(false)

		assertThrows<IllegalArgumentException> {
			reservationService.updateReservation(reservationId, interval)
		}

		verifyNoMoreInteractions(reservationDoRepository)
	}

	@Test
	fun `updateReservation should call reservationDoRepository_update and return updated ReservationDo`() {
		val reservationId = 1L
		val startInstant = Instant.now()
		val interval = startInstant ..< startInstant.plus(1, ChronoUnit.HOURS)

		`when`(fallbackReservationAvailableChecker.checkAvailable(interval)).thenReturn(true)

		val reservationDo = mock<SavedReservationDo>()
		`when`(reservationDoRepository.getById(reservationId)).thenReturn(reservationDo)

		val updatedDo = mock<SavedReservationDo>()
		`when`(reservationDoRepository.update(any(ReservationDo::class.java))).thenReturn(updatedDo)

		reservationService.updateReservation(reservationId, interval)

		verify(reservationDoRepository, times(1)).getById(reservationId)
		verify(reservationDoRepository, times(1)).update(any(ReservationDo::class.java))
	}

	// NOTE: kotlin 은 non-null으로 any()를 사용할 수 없으므로 재정의
	private fun <T> any(type: Class<T>): T = Mockito.any(type)
}
