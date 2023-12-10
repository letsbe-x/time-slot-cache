package com.letsbe.infrastructure.time.mapper

import Constants.TimeSlot.TIME_SLOT_ZONE_OFFSET
import Constants.TimeSlot.WEEK_DAY
import Constants.TimeSlot.currentBaseTime
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

object TimeSlotCacheMapper {
	fun splitByWeek(interval: OpenEndRange<Instant>): Map<Instant, OpenEndRange<Instant>> {
		val intervalList = mutableMapOf<Instant, OpenEndRange<Instant>>()

		val startAt = interval.start
		val endAt = interval.endExclusive

		var current = ZonedDateTime.ofInstant(startAt.currentBaseTime(), TIME_SLOT_ZONE_OFFSET)
		while (current.toInstant().isBefore(endAt)) {
			val baseTime = current.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toInstant()
			val start = maxOf(startAt, baseTime)
			val end = minOf(endAt, baseTime.plus(WEEK_DAY, ChronoUnit.DAYS))
			val requestInterval = start ..< end
			if (requestInterval.isEmpty().not()) {
				intervalList[baseTime] = start ..< end
			}
			current = current.plus(WEEK_DAY, ChronoUnit.DAYS)
		}
		return intervalList
	}
}
