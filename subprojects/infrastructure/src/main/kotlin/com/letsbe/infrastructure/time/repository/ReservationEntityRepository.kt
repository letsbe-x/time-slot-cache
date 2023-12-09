package com.letsbe.infrastructure.time.repository

import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.infrastructure.time.entity.ReservationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ReservationEntityRepository : JpaRepository<ReservationEntity, ReservationId> {

	// 특정 날짜에 걸쳐 있는 예약을 찾는 쿼리
	@Query("SELECT r FROM ReservationEntity r WHERE r.endAt > :dayStart AND r.startAt < :dayEnd")
	fun findReservationsOverlappingWithDay(dayStart: Instant, dayEnd: Instant): List<ReservationEntity>

	// 특정 시간 간격에 겹치는 예약을 찾는 쿼리
	@Query("SELECT r FROM ReservationEntity r WHERE r.startAt < :end AND r.endAt > :start")
	fun findReservationsOverlappingWithInterval(start: Instant, end: Instant): List<ReservationEntity>
}
