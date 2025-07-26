package com.fesi.flowit.user.web

import com.fesi.flowit.user.service.UserService
import com.fesi.flowit.user.web.request.UserRequest
import io.kotest.core.spec.style.StringSpec
import io.mockk.mockk

class UserControllerTest : StringSpec({

    "회원가입 요청을 받을 수 있다" {
        val controller = UserController(mockk<UserService>(relaxed = true))
        val request = UserRequest("user@gmail.com", "test", "password")

        controller.signUp(request)
    }
})
