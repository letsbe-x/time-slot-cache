package com.letsbe.infrastructure.sample

import com.letsbe.domain.sample.aggregate.HelloDo
import com.letsbe.domain.sample.repository.HelloDoRepository
import com.letsbe.infrastructure.sample.mapper.HelloMapper
import com.letsbe.infrastructure.sample.repository.HelloEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class HelloDoRepositoryImpl: HelloDoRepository {

	@Autowired
	lateinit var helloEntityRepository: HelloEntityRepository

	override fun getHello(nickName: String): HelloDo? {
		return helloEntityRepository.findByNickName(nickName)
			?.let(HelloMapper::toDo)
	}
}
