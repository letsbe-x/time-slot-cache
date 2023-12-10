package com.letsbe.infrastructure.time.cache.impl

import com.letsbe.domain.time.cache.TimeSlotCache
import com.letsbe.infrastructure.time.cache.TimeSlotCacheRepository
import com.letsbe.infrastructure.time.mapper.TimeSlotCacheMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.BitSet

@Repository
class TimeSlotCacheRepositoryImpl : TimeSlotCacheRepository {

	@Autowired
	lateinit var redisClient: RedisClient

	override fun getTimeSlotCache(baseTime: Instant): TimeSlotCache {
		val connection: StatefulRedisConnection<String, String> = redisClient.connect()
		val syncCommands = connection.sync()
		val bitSet = syncCommands.get(toKey(baseTime))
			?.let(TimeSlotCacheMapper::toBitSet)
			?: throw IllegalArgumentException("TimeSlotCache not found")
		return TimeSlotCache(baseTime, bitSet)
	}

	override fun setTimeSlotCache(timeSlotCache: TimeSlotCache): String {
		val connection: StatefulRedisConnection<String, String> = redisClient.connect()
		val syncCommands = connection.sync()
		val binaryString = toValue(timeSlotCache.slot)
		return syncCommands.set(timeSlotCache.key, binaryString)
	}

	private fun toKey(baseTime: Instant): String = "time-slot:${baseTime.toEpochMilli()}"
	private fun toValue(bitSet: BitSet): String = TimeSlotCacheMapper.toBinaryString(bitSet)
}
