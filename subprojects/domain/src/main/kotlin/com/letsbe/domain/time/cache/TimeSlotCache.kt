package com.letsbe.domain.time.cache

import Constants.SLOT_SIZE
import Constants.TIME_SLOT_UNIT_MINUTE
import Constants.nextIndex
import java.time.Duration
import java.time.Instant
import java.util.BitSet

data class TimeSlotCache(
	val baseTime: Instant,
	val slot: BitSet
) {
	val key: String by lazy { "time-slot:${baseTime.toEpochMilli()}" }
	val value: BitSet = slot

	fun deserialize(): Map<Instant, OpenEndRange<Instant>> {
		val timeSlots = mutableMapOf<Instant, OpenEndRange<Instant>>()
		var startIndex = findNextSetBit(0)

		while (startIndex < slot.size()) {
			val start = calculateInstant(startIndex)
			// 끝 시간을 다음 10분 간격의 시작으로 설정
			val endIndex = findNextClearBit(startIndex).nextIndex()
			val end = calculateInstant(endIndex)
			timeSlots[start] = start ..< end
			startIndex = findNextSetBit(endIndex)
		}

		return timeSlots
	}

	fun serialize(timeRanges: List<OpenEndRange<Instant>>): BitSet {
		val bitSet = BitSet(SLOT_SIZE)

		for (timeRange in timeRanges) {
			val startIndex = calculateIndex(timeRange.start)
			// 끝 시간을 전 10분 간격의 시작으로 설정 (OpenEndRange이므로)
			val endIndex = calculateIndex(timeRange.endExclusive)

			bitSet.set(startIndex, endIndex)
		}

		return bitSet
	}

	private fun findNextSetBit(fromIndex: Int): Int {
		for (i in fromIndex until slot.size()) {
			if (slot[i]) return i
		}
		return slot.size()
	}

	private fun findNextClearBit(fromIndex: Int): Int {
		for (i in fromIndex until slot.size()) {
			if (!slot[i]) return i
		}
		return slot.size()
	}

	private fun calculateInstant(index: Int): Instant {
		return baseTime.plus(Duration.ofMinutes((index * TIME_SLOT_UNIT_MINUTE).toLong()))
	}

	private fun calculateIndex(instant: Instant): Int {
		val duration = Duration.between(baseTime, instant)
		return (duration.toMinutes() / TIME_SLOT_UNIT_MINUTE).toInt()
	}
}
