package com.letsbe.infrastructure.time.repository

import com.letsbe.domain.time.aggregate.ReservationId
import com.letsbe.infrastructure.time.entity.ReservationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationEntityRepository : JpaRepository<ReservationEntity, ReservationId>
