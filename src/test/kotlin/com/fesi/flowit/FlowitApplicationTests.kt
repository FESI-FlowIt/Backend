package com.fesi.flowit

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = arrayOf("spring.config.location=classpath:application-test.yml"))
class FlowitApplicationTests {

	@Test
	fun contextLoads() {
	}

}
