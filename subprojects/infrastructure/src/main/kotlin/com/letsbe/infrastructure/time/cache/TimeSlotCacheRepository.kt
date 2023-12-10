package com.letsbe.infrastructure.time.cache

interface TimeSlotCacheRepository {
	fun get(int: Int): String
	fun set(int: Int, value: String): String
}
