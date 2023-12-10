package com.letsbe.infrastructure.config

import io.lettuce.core.RedisClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedisConfig {
	@Bean
	fun redisClient(): RedisClient {
		return RedisClient.create("redis://localhost")
	}
}
