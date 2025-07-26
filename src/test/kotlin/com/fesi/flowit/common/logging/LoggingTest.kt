package com.fesi.flowit.common.logging

import io.kotest.core.spec.style.StringSpec
import org.springframework.test.context.TestPropertySource

private val logWithClassName = loggerFor<LoggingTest>()

@TestPropertySource(properties = arrayOf("spring.config.location=classpath:application-test.yml"))
class LoggingTest : StringSpec({
    "로거를 정의할 수 있다" {
        val log = logger()
        log.debug("debug 레벨 로그")
        log.info("info 레벨 로그")
        log.warn("warn 레벨 로그")
        log.error("error 레벨 로그")
    }

    "클래스명을 명시해서 로거를 정의할 수 있다" {
        logWithClassName.debug("debug 레벨 로그")
        logWithClassName.info("info 레벨 로그")
        logWithClassName.warn("warn 레벨 로그")
        logWithClassName.error("error 레벨 로그")
    }
})
