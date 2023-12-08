package com.letsbe.infrastructure.sample.repository

import jakarta.transaction.Transactional
import com.letsbe.infrastructure.sample.HelloEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface HelloEntityRepository: JpaRepository<HelloEntity, Long> {
	fun findByNickName(nickName: String): HelloEntity?
}
