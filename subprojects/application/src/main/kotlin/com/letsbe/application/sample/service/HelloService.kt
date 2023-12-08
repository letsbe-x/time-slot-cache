package com.letsbe.application.sample.service

import com.letsbe.domain.sample.aggregate.HelloDo
import com.letsbe.domain.sample.repository.HelloDoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HelloService {

	@Autowired
	lateinit var helloDoRepository: HelloDoRepository

	fun sayHello(searchName: String?): String {
		return searchName?.let{
			helloDoRepository.getHello(searchName)?.toString()
		} ?: HelloDo.default.toString()
	}
}
