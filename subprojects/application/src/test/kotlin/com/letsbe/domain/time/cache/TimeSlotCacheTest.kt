package com.letsbe.domain.time.cache

import Constants.TimeSlot.SLOT_SIZE
import Constants.TimeSlot.TIME_SLOT_UNIT_MINUTE
import Constants.TimeSlot.nextIndex
import Constants.TimeSlot.preIndex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.BitSet

class TimeSlotCacheTest {

	@Test
	fun testIsAvailable() {
		val baseTime = Instant.parse("2023-12-10T00:00:00Z")
		val startIndex = 60
		val endIndex = 72.preIndex() // OpenEndRange이므로 endIndex는 전 10분 간격의 시작으로 설정
		val bitSet = BitSet(SLOT_SIZE).apply {
			// 2023-12-10T10:00: ~ 2023-12-10T11:50:00까지 예약 불가능으로 설정 [예약은 2023-12-10T12:00:00부터 가능]
			set(startIndex, endIndex)
		}
		val timeSlotCache = TimeSlotCache(baseTime, bitSet)

		val result = timeSlotCache.isAvailable(
			Instant.parse("2023-12-10T10:00:00Z") ..< Instant.parse("2023-12-10T11:00:00Z")
		)
		assertEquals(false, result)

		val result2 = timeSlotCache.isAvailable(
			Instant.parse("2023-12-10T00:00:00Z") ..< Instant.parse("2023-12-10T10:00:00Z")
		)
		assertEquals(true, result2)

		val result3 = timeSlotCache.isAvailable(
			Instant.parse("2023-12-10T12:00:00Z") ..< Instant.parse("2023-12-10T23:59:59Z")
		)
		assertEquals(true, result3)
	}

	@Test
	fun testDeserialize() {
		val baseTime = Instant.parse("2023-12-10T00:00:00Z")
		val startIndex = 60
		val endIndex = 72.preIndex() // OpenEndRange이므로 endIndex는 전 10분 간격의 시작으로 설정
		val bitSet = BitSet(SLOT_SIZE).apply {
			set(startIndex, endIndex) // 2023-12-10T10:00: ~ 2023-12-10T11:50:00까지 예약 불가능으로 설정 [예약은 2023-12-10T12:00:00부터 가능]
		}
		val timeSlotCache = TimeSlotCache(baseTime, bitSet)

		val expectedStart = baseTime.plus((startIndex * TIME_SLOT_UNIT_MINUTE).toLong(), ChronoUnit.MINUTES) // 2023-12-10T10:00:00
		val expectedEnd = baseTime.plus((endIndex.nextIndex() * TIME_SLOT_UNIT_MINUTE).toLong(), ChronoUnit.MINUTES) // 2023-12-10T12:00:00
		val expected = mapOf(expectedStart to expectedStart ..< expectedEnd)

		val result = timeSlotCache.deserialize()

		// 결과 검증
		assertEquals(endIndex - startIndex, bitSet.cardinality())
		assertEquals(expected, result)
	}

	@Test
	fun `testDeserialize - multi`() {
		val baseTime = Instant.parse("2023-12-10T00:00:00Z")
		val startIndex = 60
		val endIndex = 72.preIndex() // OpenEndRange이므로 endIndex는 전 10분 간격의 시작으로 설정
		val bitSet = BitSet(SLOT_SIZE).apply {
			set(startIndex, endIndex) // 2023-12-10T10:00: ~ 2023-12-10T11:50:00까지 예약 불가능으로 설정 [예약은 2023-12-10T12:00:00부터 가능]
		}
		val timeSlotCache = TimeSlotCache(baseTime, bitSet)

		val expectedStart = baseTime.plus((startIndex * TIME_SLOT_UNIT_MINUTE).toLong(), ChronoUnit.MINUTES) // 2023-12-10T10:00:00
		val expectedEnd = baseTime.plus((endIndex.nextIndex() * TIME_SLOT_UNIT_MINUTE).toLong(), ChronoUnit.MINUTES) // 2023-12-10T12:00:00
		val expected = mapOf(expectedStart to expectedStart ..< expectedEnd)

		val result = timeSlotCache.deserialize()

		// 결과 검증
		assertEquals(endIndex - startIndex, bitSet.cardinality())
		assertEquals(expected, result)
	}

	@Test
	fun testSerialize() {
		val baseTime = Instant.parse("2023-12-10T00:00:00Z")
		val timeSlotCache = TimeSlotCache(baseTime, BitSet(SLOT_SIZE))
		val expectedStartIndex = 60
		val expectedEndIndex = 72.preIndex() // OpenEndRange이므로 endIndex는 전 10분 간격의 시작으로 설정
		val startAt = baseTime.plus(/* amountToAdd = */ (expectedStartIndex * TIME_SLOT_UNIT_MINUTE).toLong(), /* unit = */ ChronoUnit.MINUTES) // 2023-12-10T10:00:00
		val endAt = baseTime.plus(((expectedEndIndex.nextIndex()) * TIME_SLOT_UNIT_MINUTE).toLong(), ChronoUnit.MINUTES) // 2023-12-10T12:00:00
		val timeRange = startAt ..< endAt

		val bitSet = timeSlotCache.serialize(listOf(timeRange))

		// 나머지 비트는 설정되지 않았는지 확인
		for (i in 0..(expectedStartIndex.preIndex())) {
			assertEquals(false, bitSet[i], "Bit at index $i should not be set")
		}

		// 60번째부터 71번째 비트까지 확인
		for (i in expectedStartIndex..expectedEndIndex) {
			assertEquals(true, bitSet[i], "Bit at index $i should be set")
		}

		for (i in (expectedEndIndex.nextIndex())..bitSet.size()) {
			assertEquals(false, bitSet[i], "Bit at index $i should not be set")
		}
	}
}
