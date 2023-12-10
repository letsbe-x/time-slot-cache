package com.letsbe.infrastructure.time.cache

import com.letsbe.domain.time.cache.TimeSlotCache
import java.time.Instant

interface TimeSlotCacheRepository {
	fun getTimeSlotCache(baseTime: Instant): TimeSlotCache
	fun setTimeSlotCache(timeSlotCache: TimeSlotCache): String
}
