package com.fesi.flowit.common.config

import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = arrayOf("spring.config.location=classpath:application-test.yml"))
class SecurityConfigTest @Autowired constructor(
    val mockMvc: MockMvc
) : StringSpec({

    //TODO
    // 인증 기능 구현 시 코드에 반영해야 한다
    "모든 api 호출을 인증 없이 통과시킨다" {
        mockMvc.perform(
            get("/nonexistent")
        ).andExpect(status().isNotFound)
    }
})
