package com.letsbe.infrastructure.time.cache.impl

import com.letsbe.infrastructure.time.cache.TimeSlotCacheRepository
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class TimeSlotCacheRepositoryImpl : TimeSlotCacheRepository {

	@Autowired
	lateinit var redisClient: RedisClient

	override fun get(int: Int): String {
		val connection: StatefulRedisConnection<String, String> = redisClient.connect()
		val syncCommands = connection.sync()
		return syncCommands.get("sample:$int")
	}

	override fun set(int: Int, value: String): String {
		val connection: StatefulRedisConnection<String, String> = redisClient.connect()
		val syncCommands = connection.sync()
		return syncCommands.set("sample:$int", value)
	}
}
