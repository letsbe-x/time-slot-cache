package com.letsbe.infrastructure

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	classes = [TestingApplication::class]
)
@ActiveProfiles("test")
abstract class AbstractRepositoryTest
