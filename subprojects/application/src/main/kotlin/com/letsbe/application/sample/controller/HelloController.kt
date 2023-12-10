package com.letsbe.application.sample.controller

import com.letsbe.application.sample.dto.HelloRequest
import com.letsbe.application.sample.service.HelloService
import com.letsbe.infrastructure.time.cache.TimeSlotCacheRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

	@Autowired
	lateinit var helloService: HelloService

	@Autowired
	lateinit var timeSlotCacheRepository: TimeSlotCacheRepository

	@GetMapping("hello")
	fun hello(
		request: HelloRequest
	): ResponseEntity<String> {
		return ResponseEntity.ok(
			helloService.sayHello(request.searchName)
		)
	}

	@GetMapping("hello-cache/{id}")
	fun helloCache(
		@PathVariable id: Int
	): ResponseEntity<String> {
		return ResponseEntity.ok(
			timeSlotCacheRepository.get(id)
		)
	}

	@PostMapping("hello-cache/{id}")
	fun helloCachePost(
		@PathVariable id: Int,
		@RequestBody body: String
	): ResponseEntity<String> {
		return ResponseEntity.ok(
			timeSlotCacheRepository.set(id, body)
		)
	}
}
