package com.letsbe.infrastructure.time.cache.impl

import com.letsbe.domain.time.cache.TimeSlotCache
import com.letsbe.infrastructure.time.cache.TimeSlotCacheRepository
import com.letsbe.infrastructure.utils.RedisUtils
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
			?.let { getValue(it) }
			?: BitSet()
		return TimeSlotCache(baseTime, bitSet)
	}

	override fun setTimeSlotCache(timeSlotCache: TimeSlotCache) {
		val connection: StatefulRedisConnection<String, String> = redisClient.connect()
		val syncCommands = connection.sync()
		val binaryString = toValue(timeSlotCache.slot)
		syncCommands.set(timeSlotCache.key, binaryString)
	}

	override fun updateTimeSlotCache(baseTime: Instant, interval: OpenEndRange<Instant>, method: EnumCacheUpdateMethod) {
		val connection: StatefulRedisConnection<String, String> = redisClient.connect()
		val syncCommands = connection.sync()

		val timeSlotCache = getTimeSlotCache(baseTime)
		val bitSet = timeSlotCache.slot
		val requestBitSet = timeSlotCache.serialize(listOf(interval))

		// syncCommands.bitfield()를 사용하여, 필요한 비트만 설정하는 방법도 있음
		when (method) {
			EnumCacheUpdateMethod.INSERT -> bitSet.or(requestBitSet)
			EnumCacheUpdateMethod.DELETE -> bitSet.andNot(requestBitSet)
		}

		val binaryString = toValue(bitSet)
		syncCommands.set(toKey(baseTime), binaryString)
	}

	enum class EnumCacheUpdateMethod {
		INSERT,
		DELETE
	}

	private fun toKey(baseTime: Instant): String = TimeSlotCache.getKey(baseTime)
	private fun getValue(string: String): BitSet = RedisUtils.fromByteArrayReverse(string.toByteArray())
	private fun toValue(bitSet: BitSet): String = RedisUtils.toByteArrayReverse(bitSet).decodeToString()
}
