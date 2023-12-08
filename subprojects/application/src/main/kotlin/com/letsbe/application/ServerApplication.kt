package com.letsbe.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(
	scanBasePackages = ["com.letsbe"]
)
@EntityScan(basePackages = ["com.letsbe.infrastructure.entity"])
@EnableJpaRepositories(basePackages = ["com.letsbe.infrastructure.repository"])
class ServerApplication

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)
}
