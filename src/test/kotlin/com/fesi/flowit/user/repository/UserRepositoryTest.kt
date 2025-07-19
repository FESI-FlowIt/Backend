package com.fesi.flowit.user.repository

import com.fesi.flowit.user.entity.User
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.time.LocalDateTime

@Transactional
@SpringBootTest
@TestPropertySource(properties = arrayOf("spring.config.location=classpath:application-test.yml"))
class UserRepositoryTest @Autowired constructor(
     val userRepository: UserRepository
) : StringSpec({

    beforeEach {
        userRepository.deleteAll()
    }

    "사용자 등록 시 삭제 일시는 설정하지 않는다" {
        val user = User("test@example.com", "홍길동", "password123", LocalDateTime.now(), LocalDateTime.now(), null)

        val savedUser = userRepository.save(user)

        savedUser.createdAt.shouldNotBeNull()
        savedUser.updatedAt.shouldNotBeNull()
        savedUser.deletedAt.shouldBeNull()
    }

    "이메일로 사용자 정보를 검색할 수 있다" {
        val user = User("test@example.com", "홍길동", "password123", LocalDateTime.now(), LocalDateTime.now(), null)
        val savedUser = userRepository.save(user)

        val userFoundByEmail = userRepository.findByEmail(savedUser.email)

        userFoundByEmail?.shouldBeEqual(savedUser)
    }

    "등록되지 않은 사용자의 이메일로 검색할 수 없다" {
        val userFoundByEmail = userRepository.findByEmail("test@example.com")

        userFoundByEmail.shouldBeNull()
    }
})
