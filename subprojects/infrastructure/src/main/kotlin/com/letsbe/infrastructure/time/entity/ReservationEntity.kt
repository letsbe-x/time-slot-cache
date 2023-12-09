package com.letsbe.infrastructure.time.entity

import com.letsbe.domain.time.aggregate.ReservationId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.Instant

@Table(name = "reservation_info")
@Entity
class ReservationEntity(
	id: ReservationId? = null,
	startAt: Instant,
	endAt: Instant
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: ReservationId? = id

	@Column(
		nullable = false,
		name = "start_at"
	)
	var startAt: Instant = startAt

	@Column(
		nullable = false,
		name = "end_at"
	)
	var endAt: Instant = endAt

	@Column(
		nullable = false,
		updatable = false,
		name = "created_at"
	)
	lateinit var createdAt: Instant
		private set

	@Column(
		nullable = false,
		name = "updated_at"
	)
	lateinit var updatedAt: Instant
		private set

	@PrePersist
	private fun onPrePersist() {
		createdAt = Instant.now()
		updatedAt = createdAt
	}

	@PreUpdate
	private fun onPreUpdate() {
		updatedAt = Instant.now()
	}
}
