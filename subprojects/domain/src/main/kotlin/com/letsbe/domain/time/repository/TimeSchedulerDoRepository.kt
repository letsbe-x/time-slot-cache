package com.letsbe.domain.time.repository

import com.letsbe.domain.time.aggregate.TimeSchedulerDo
import java.time.Instant
import java.util.Date

interface TimeSchedulerDoRepository {
	fun findByInterval(startAt: Instant, endAt: Instant): List<TimeSchedulerDo>
	fun findByDate(date: Date): TimeSchedulerDo
}
