package com.letsbe.infrastructure.time.mapper

import Constants.TimeSlot.TIME_SLOT_ZONE_OFFSET
import com.letsbe.infrastructure.time.mapper.TimeSlotCacheMapper.splitByWeek
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

class TimeSlotCacheMapperTest {
	@Test
	fun `splitByWeek should split single week interval correctly`() {
		val now = Instant.parse("2023-12-10T00:00:00Z")
		val sunday = ZonedDateTime.ofInstant(now, TIME_SLOT_ZONE_OFFSET).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toInstant()
		val nextSunday = ZonedDateTime.ofInstant(now, TIME_SLOT_ZONE_OFFSET).with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).toInstant()
		val interval = sunday..<nextSunday

		val expectedMap = mapOf(
			now to interval
		)

		val actualMap = splitByWeek(interval)

		assertEquals(expectedMap, actualMap)
	}

	@Test
	fun `splitByWeek should split multiple week interval correctly`() {
		val now = Instant.parse("2023-12-10T00:00:00Z")
		val sunday = ZonedDateTime.ofInstant(now, TIME_SLOT_ZONE_OFFSET).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toInstant()
		val nextSunday = ZonedDateTime.ofInstant(now, TIME_SLOT_ZONE_OFFSET).with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).toInstant()
		val nextNextSunday = nextSunday.plus(7, ChronoUnit.DAYS)
		val interval = sunday..<nextNextSunday

		val expectedMap = mapOf(
			now to sunday..<nextSunday,
			nextSunday to nextSunday..<nextNextSunday
		)

		val actualMap = splitByWeek(interval)

		assertEquals(expectedMap, actualMap)
	}

	@Test
	fun `splitByWeek should handle interval end on Saturday`() {
		val startAt = Instant.parse("2023-12-09T12:00:00Z")
		val endAt = Instant.parse("2023-12-16T12:00:00Z")

		val interval = startAt..<endAt
		val baseTime = ZonedDateTime.ofInstant(startAt, TIME_SLOT_ZONE_OFFSET).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toInstant()
		val nextBaseTime = ZonedDateTime.ofInstant(endAt, TIME_SLOT_ZONE_OFFSET).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toInstant()

		val expectedMap = mapOf(
			baseTime to startAt..<nextBaseTime,
			nextBaseTime to nextBaseTime..<endAt
		)

		val actualMap = splitByWeek(interval)

		assertEquals(expectedMap, actualMap)
	}

	@Test
	fun `splitByWeek should handle empty interval`() {
		val monday = Instant.parse("2023-12-10T00:00:00Z")
		val interval = monday..<monday

		val expectedMap = emptyMap<Instant, OpenEndRange<Instant>>()

		val actualMap = splitByWeek(interval)

		assertEquals(expectedMap, actualMap)
	}
}
