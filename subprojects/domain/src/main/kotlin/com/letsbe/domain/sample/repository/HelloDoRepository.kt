package com.letsbe.domain.sample.repository

import com.letsbe.domain.sample.aggregate.HelloDo

interface HelloDoRepository {
	fun getHello(nickName: String): HelloDo?
}
