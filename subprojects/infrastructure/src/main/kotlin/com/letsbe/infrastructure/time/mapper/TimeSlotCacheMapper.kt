package com.letsbe.infrastructure.time.mapper

import Constants.DISABLE
import Constants.ENABLE
import Constants.SLOT_SIZE
import java.util.BitSet

object TimeSlotCacheMapper {
	fun toBitSet(binaryString: String): BitSet {
		return BitSet(SLOT_SIZE).apply {
			binaryString.forEachIndexed { index, c ->
				if (c == DISABLE) set(index)
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
}
