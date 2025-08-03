package com.fesi.flowit.common.config

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotBeIn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = arrayOf("spring.config.location=classpath:application-test.yml"))
class SecurityConfigTest @Autowired constructor(
    val mockMvc: MockMvc
) : StringSpec({

    "화이트리스트에 포함되지 않은 api 호출은 무조건 인증한다" {
        mockMvc.perform(
            get("/nonexistent")
        ).andExpect(status().isNotFound)
    }

    "화이트리스트에 포함된 api 호출은 인증을 건너뛴다" {
        val apiWhiteList = arrayOf(post("/auths/signIn"), post("/users"))
        apiWhiteList.forEach { apiCall ->
            val result = mockMvc.perform(
                apiCall
            ).andReturn()

            (result.response.status).shouldNotBeIn(401, 403)
        }
    }
})
