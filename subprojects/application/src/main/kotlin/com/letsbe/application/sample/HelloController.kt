package com.letsbe.application.com.letsbe.application.sample

import com.letsbe.domain.sample.dto.HelloDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

	@GetMapping("hello")
	fun hello(
		request: HelloDto,
	): ResponseEntity<String> {
		return ResponseEntity.ok("Hello, ${request.name}!")
	}
}
