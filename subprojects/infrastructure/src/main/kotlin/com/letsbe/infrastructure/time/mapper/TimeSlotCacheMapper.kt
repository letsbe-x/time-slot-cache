package com.letsbe.infrastructure.time.mapper

import Constants.TimeSlot.DISABLE
import Constants.TimeSlot.ENABLE
import Constants.TimeSlot.SLOT_SIZE
import Constants.TimeSlot.TIME_SLOT_ZONE_OFFSET
import Constants.TimeSlot.WEEK_DAY
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.BitSet

object TimeSlotCacheMapper {
	fun toBitSet(binaryString: String): BitSet {
		return BitSet(SLOT_SIZE).apply {
			binaryString.forEachIndexed { index, c ->
				if (c == DISABLE.toInt().toChar()) set(index)
			}
		}
	}

	fun toBinaryString(bitSet: BitSet): String {
		return buildString {
			for (i in 0 until SLOT_SIZE) {
				append(if (bitSet[i]) DISABLE else ENABLE)
			}
		}
	}

	fun splitByWeek(interval: OpenEndRange<Instant>): Map<Instant, OpenEndRange<Instant>> {
		val intervalList = mutableMapOf<Instant, OpenEndRange<Instant>>()

		val startAt = interval.start
		val endAt = interval.endExclusive

		var current = ZonedDateTime.ofInstant(interval.start, TIME_SLOT_ZONE_OFFSET)
			.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
			.truncatedTo(ChronoUnit.DAYS)
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
