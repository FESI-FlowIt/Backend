package com.fesi.flowit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class FlowitApplication

fun main(args: Array<String>) {
	runApplication<FlowitApplication>(*args)
}
