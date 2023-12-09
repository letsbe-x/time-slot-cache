package com.letsbe.infrastructure.sample.mapper

import com.letsbe.domain.sample.aggregate.HelloDo
import com.letsbe.infrastructure.sample.HelloEntity

object HelloMapper {
	fun toDo(helloEntity: HelloEntity): HelloDo {
		return HelloDo(
			id = helloEntity.id!!,
			nickName = helloEntity.nickName
		)
	}
}
