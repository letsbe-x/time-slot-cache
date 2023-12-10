import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

object Constants {
	object TimeSlot {
		// 10분 단위로 예약 가능
		const val TIME_SLOT_UNIT_MINUTE = 10
		const val NOT_FOUND_BIT = -1
		const val WEEK_DAY = 7L

		val TIME_SLOT_ZONE_OFFSET: ZoneOffset = ZoneOffset.UTC

		fun Int.nextIndex(): Int {
			return this + 1
		}
		fun Int.preIndex(): Int {
			return this - 1
		}

		// TimeSlotCache의 baseTime은 현재 WEEK_DAY로 되어있고, 기준시간은 항상 일요일 00:00:00Z를 만족
		fun Instant.currentBaseTime(): Instant {
			return ZonedDateTime.ofInstant(this, TIME_SLOT_ZONE_OFFSET)
				.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
				.truncatedTo(ChronoUnit.DAYS)
				.toInstant()
		}

		fun Instant.nextBaseTime(): Instant {
			return ZonedDateTime.ofInstant(this, TIME_SLOT_ZONE_OFFSET)
				.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
				.truncatedTo(ChronoUnit.DAYS)
				.toInstant()
		}
	}
}
