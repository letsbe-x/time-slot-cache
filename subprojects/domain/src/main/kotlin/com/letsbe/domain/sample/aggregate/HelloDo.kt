package com.letsbe.domain.sample.aggregate

typealias HelloId = Long
data class HelloDo(
	val id: HelloId? = null,
	val nickName: String
) {
	override fun toString(): String {
		return "Hello $nickName!"
	}

	companion object {
		val default = HelloDo(
			nickName = "World"
		)
	}
}
