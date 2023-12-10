package com.letsbe.infrastructure.time.cache

import com.letsbe.domain.time.cache.TimeSlotCache
import com.letsbe.infrastructure.time.cache.impl.TimeSlotCacheRepositoryImpl.EnumCacheUpdateMethod
import java.time.Instant

interface TimeSlotCacheRepository {
	fun getTimeSlotCache(baseTime: Instant): TimeSlotCache
	fun setTimeSlotCache(timeSlotCache: TimeSlotCache)
	fun updateTimeSlotCache(baseTime: Instant, interval: OpenEndRange<Instant>, method: EnumCacheUpdateMethod)
}
