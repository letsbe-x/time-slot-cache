object Constants {
	// 10분 단위로 예약 가능
	const val TIME_SLOT_UNIT_MINUTE = 10

	fun Int.nextIndex(): Int {
		return this + 1
	}
	fun Int.preIndex(): Int {
		return this - 1
	}
}
