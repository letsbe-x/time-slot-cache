package com.letsbe.infrastructure.utils

import java.util.BitSet
import kotlin.experimental.and
import kotlin.experimental.or

object RedisUtils {
	/**
	 * Java와 Redis는 bit를 역순으로 저장한다.
	 * ref: https://github.com/redis/jedis/issues/301
	 */
	fun fromByteArrayReverse(bytes: ByteArray): BitSet {
		val bits = BitSet(bytes.size * 8)
		for (i in 0 until bytes.size * 8) {
			if ((bytes[i / 8] and ((1 shl (7 - (i % 8))).toByte())) > 0) {
				bits.set(i)
			}
		}
		return bits
	}

	fun toByteArrayReverse(bits: BitSet): ByteArray {
		val byteSize = (bits.length() + 7) / 8
		val bytes = ByteArray(byteSize)
		for (i in 0 until bits.length()) {
			if (bits.get(i)) {
				val byteIndex = i / 8
				val mask = 1 shl (7 - (i % 8))
				bytes[byteIndex] = (bytes[byteIndex] or mask.toByte())
			}
		}
		return bytes
	}
}
