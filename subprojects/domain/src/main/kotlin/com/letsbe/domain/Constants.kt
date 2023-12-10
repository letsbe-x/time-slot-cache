import java.time.ZoneOffset

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
	}
}
