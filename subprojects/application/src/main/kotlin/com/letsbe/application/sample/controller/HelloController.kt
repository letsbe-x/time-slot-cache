package com.letsbe.application.sample.controller

import com.letsbe.application.sample.dto.HelloRequest
import com.letsbe.application.sample.service.HelloService
import com.letsbe.domain.sample.aggregate.HelloDo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

	@Autowired
	lateinit var helloService: HelloService

	@GetMapping("hello")
	fun hello(
		request: HelloRequest,
	): ResponseEntity<String> {
		return ResponseEntity.ok(
			helloService.sayHello(request.searchName)
		)
	}
}
