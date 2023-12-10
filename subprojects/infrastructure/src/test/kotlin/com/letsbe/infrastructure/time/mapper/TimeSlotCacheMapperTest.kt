package com.letsbe.infrastructure.time.mapper

import Constants.TimeSlot.currentBaseTime
import Constants.TimeSlot.nextBaseTime
import com.letsbe.infrastructure.time.mapper.TimeSlotCacheMapper.splitByWeek
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class TimeSlotCacheMapperTest {
	@Test
	fun `splitByWeek should split single week interval correctly`() {
		val currentTime = Instant.parse("2023-12-10T00:00:00Z")
		val sunday = currentTime.currentBaseTime()
		val nextSunday = currentTime.nextBaseTime()
		val interval = sunday..<nextSunday

		val expectedMap = mapOf(
			currentTime to interval
		)

		val actualMap = splitByWeek(interval)

		assertEquals(expectedMap, actualMap)
	}

	@Test
	fun `splitByWeek should split multiple week interval correctly`() {
		val currentTime = Instant.parse("2023-12-10T00:00:00Z")
		val sunday = currentTime.currentBaseTime()
		val nextSunday = currentTime.nextBaseTime()
		val nextNextSunday = nextSunday.plus(7, ChronoUnit.DAYS)
		val interval = sunday..<nextNextSunday

		val expectedMap = mapOf(
			sunday to sunday..<nextSunday,
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
		val baseTime = startAt.currentBaseTime()
		val nextBaseTime = startAt.nextBaseTime()

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
