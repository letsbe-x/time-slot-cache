package com.letsbe.domain.time.cache

import Constants.TimeSlot.NOT_FOUND_BIT
import Constants.TimeSlot.SLOT_SIZE
import Constants.TimeSlot.TIME_SLOT_UNIT_MINUTE
import Constants.TimeSlot.TIME_SLOT_ZONE_OFFSET
import Constants.TimeSlot.nextIndex
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.BitSet

data class TimeSlotCache(
	val baseTime: Instant,
	val slot: BitSet
) {
	val key: String by lazy {
		ZonedDateTime.ofInstant(baseTime, TIME_SLOT_ZONE_OFFSET).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).truncatedTo(ChronoUnit.DAYS)
			.run { "time-slot:${toInstant()}" }
	}
	val value: Map<Instant, OpenEndRange<Instant>> by lazy { deserialize() }

	fun isAvailable(interval: OpenEndRange<Instant>): Boolean {
		val startIndex = calculateIndex(interval.start)
		val endIndex = calculateIndex(interval.endExclusive)

		// endIndex는 범위의 끝을 포함하지 않으므로, 실제 체크할 때는 endIndex - 1까지만 체크
		// nextSetBit를 사용하여 주어진 범위 내에서 설정된 첫 번째 비트의 인덱스를 찾음
		val nextSetBitIndex = slot.nextSetBit(startIndex)

		// nextSetBitIndex가 endIndex보다 작거나 같으면, 해당 범위 내에 설정된 비트가 있음을 의미
		return nextSetBitIndex == NOT_FOUND_BIT || nextSetBitIndex >= endIndex
	}

	fun deserialize(): Map<Instant, OpenEndRange<Instant>> {
		val timeSlots = mutableMapOf<Instant, OpenEndRange<Instant>>()
		var startIndex = slot.nextSetBit(0)

		while (startIndex != NOT_FOUND_BIT && startIndex < slot.size()) {
			val start = calculateInstant(startIndex)
			// 끝 시간을 다음 10분 간격의 시작으로 설정
			val endIndex = slot.nextClearBit(startIndex).nextIndex()
			val end = calculateInstant(endIndex)
			timeSlots[start] = start ..< end

			startIndex = slot.nextSetBit(endIndex)
		}

		return timeSlots
	}

	fun serialize(timeRanges: List<OpenEndRange<Instant>>): BitSet {
		val bitSet = BitSet(SLOT_SIZE)

		for (timeRange in timeRanges) {
			val startIndex = calculateIndex(timeRange.start)
			// 끝 시간을 전 10분 간격의 시작으로 설정 (OpenEndRange이므로)
			val endIndex = calculateIndex(timeRange.endExclusive)
			// startIndex..<endIndex
			bitSet.set(startIndex, endIndex)
		}

		return bitSet
	}

	private fun calculateInstant(index: Int): Instant {
		return baseTime.plus((index * TIME_SLOT_UNIT_MINUTE).toLong(), ChronoUnit.MINUTES)
	}

	private fun calculateIndex(instant: Instant): Int {
		val duration = Duration.between(baseTime, instant)
		return (duration.toMinutes() / TIME_SLOT_UNIT_MINUTE).toInt()
	}

	companion object {
		fun getKey(baseTime: Instant): String = ZonedDateTime.ofInstant(baseTime, TIME_SLOT_ZONE_OFFSET).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).truncatedTo(ChronoUnit.DAYS)
			.run { "time-slot:${toInstant()}" }
	}
}
