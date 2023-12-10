package com.letsbe.infrastructure.utils

import io.lettuce.core.codec.RedisCodec
import java.nio.ByteBuffer
import java.nio.charset.Charset

class StringByteCodec : RedisCodec<String, ByteArray> {
	private val charset = Charset.forName("UTF-8")
	override fun decodeKey(bytes: ByteBuffer): String {
		return charset.decode(bytes).toString()
	}

	override fun decodeValue(bytes: ByteBuffer): ByteArray {
		return getBytes(bytes)
	}

	override fun encodeKey(key: String): ByteBuffer {
		return charset.encode(key)
	}

	override fun encodeValue(value: ByteArray?): ByteBuffer {
		return if (value == null) {
			ByteBuffer.wrap(EMPTY)
		} else {
			ByteBuffer.wrap(value)
		}
	}

	companion object {
		val INSTANCE = StringByteCodec()
		private val EMPTY = ByteArray(0)
		private fun getBytes(buffer: ByteBuffer): ByteArray {
			val b = ByteArray(buffer.remaining())
			buffer.get(b)
			return b
		}
	}
}
