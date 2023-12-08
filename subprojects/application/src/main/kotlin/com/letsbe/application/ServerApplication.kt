package com.letsbe.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication(
	scanBasePackages = ["com.letsbe"]
)
@EntityScan(basePackages = ["com.letsbe.domain"])
// @EnableJpaRepositories(basePackages = ["com.letsbe.infrastructure"])
class ServerApplication

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)
}
