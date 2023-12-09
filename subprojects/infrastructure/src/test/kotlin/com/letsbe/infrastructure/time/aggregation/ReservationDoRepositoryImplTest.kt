package com.letsbe.infrastructure.time.aggregation

import com.letsbe.domain.time.aggregate.ReservationDo
import com.letsbe.infrastructure.AbstractRepositoryTest
import com.letsbe.infrastructure.time.entity.ReservationEntity
import com.letsbe.infrastructure.time.mapper.ReservationMapper
import com.letsbe.infrastructure.time.repository.ReservationEntityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.Instant
import java.util.Optional
import kotlin.NoSuchElementException

class ReservationDoRepositoryImplTest : AbstractRepositoryTest() {

	@InjectMocks
	private lateinit var reservationDoRepositoryImpl: ReservationDoRepositoryImpl

	@Mock
	private lateinit var reservationEntityRepository: ReservationEntityRepository

	@Test
	fun `getById should return reservation when it exists`() {
		val reservationId = 1L
		val reservationEntity = ReservationEntity(id = reservationId, startAt = Instant.now(), endAt = Instant.now().plusSeconds(3600))
		val expectedReservationDo = ReservationMapper.toDo(reservationEntity)
		`when`(reservationEntityRepository.findById(reservationId)).thenReturn(Optional.of(reservationEntity))

		val result = reservationDoRepositoryImpl.getById(reservationId)

		assertEquals(expectedReservationDo, result)
	}

	@Test
	fun `getById should throw exception when reservation does not exist`() {
		val reservationId = 1L
		`when`(reservationEntityRepository.findById(reservationId)).thenReturn(Optional.empty())

		assertThrows<NoSuchElementException> {
			reservationDoRepositoryImpl.getById(reservationId)
		}
	}

	@Test
	fun `deleteById should delete reservation if it exists`() {
		val reservationId = 1L
		val reservationEntity = ReservationEntity(id = reservationId, startAt = Instant.now(), endAt = Instant.now().plusSeconds(3600))
		`when`(reservationEntityRepository.findById(reservationId)).thenReturn(Optional.of(reservationEntity))
		Mockito.doNothing().`when`(reservationEntityRepository).deleteById(reservationId)

		reservationDoRepositoryImpl.deleteById(reservationId)

		Mockito.verify(reservationEntityRepository).deleteById(reservationId)
	}

	@Test
	fun `update should update and return reservation`() {
		val reservationId = 1L
		val reservationDo = ReservationDo(id = reservationId, interval = Instant.now() ..< Instant.now().plusSeconds(3600))
		val reservationEntity = ReservationMapper.toEntity(reservationDo)
		`when`(reservationEntityRepository.findById(reservationId)).thenReturn(Optional.of(reservationEntity))
		`when`(reservationEntityRepository.save(any())).thenReturn(reservationEntity)

		val result = reservationDoRepositoryImpl.update(reservationDo)

		assertEquals(reservationDo, result)
	}
}
