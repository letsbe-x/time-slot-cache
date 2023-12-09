package com.letsbe.infrastructure.time.entity

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@ExtendWith(SpringExtension::class)
@DataJpaTest
@TestPropertySource(locations = ["classpath:application-test.yml"])
class ReservationEntityTest
@Autowired constructor(
	val entityManager: TestEntityManager
) {
	@Test
	fun `when saved, createdAt and updatedAt should be set`() {
		val reservation = ReservationEntity(startAt = Instant.now(), endAt = Instant.now().plusSeconds(3600))
		entityManager.persistAndFlush(reservation)

		assertNotNull(reservation.createdAt)
		assertNotNull(reservation.updatedAt)
		assertEquals(reservation.createdAt, reservation.updatedAt)
	}

	@Test
	fun `when updated, updatedAt should change`() {
		val reservation = ReservationEntity(startAt = Instant.now(), endAt = Instant.now().plusSeconds(3600))
		entityManager.persistAndFlush(reservation)

		val initialUpdatedAt = reservation.updatedAt
		Thread.sleep(1000)
		reservation.endAt = Instant.now().plusSeconds(7200)
		entityManager.persistAndFlush(reservation)

		assertNotEquals(initialUpdatedAt, reservation.updatedAt)
	}
}
