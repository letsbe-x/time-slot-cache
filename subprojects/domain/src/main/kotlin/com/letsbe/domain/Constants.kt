object Constants {
	object TimeSlot {
		// 10분 단위로 예약 가능
		const val TIME_SLOT_UNIT_MINUTE = 10
		const val SLOT_SIZE = 1024 // 7일 * 24시간 * 6(10분 단위) = 1008 + 16(BLANK) (unit byte 1024 bit)
		const val DISABLE = '1'
		const val ENABLE = '0'
		const val NOT_FOUND_BIT = -1

		fun Int.nextIndex(): Int {
			return this + 1
		}
		fun Int.preIndex(): Int {
			return this - 1
		}
	}
}
