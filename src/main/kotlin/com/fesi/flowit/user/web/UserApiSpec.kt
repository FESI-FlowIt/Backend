package com.fesi.flowit.user.web

import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.user.web.request.UserRequest
import com.fesi.flowit.user.web.response.UserResponse
import com.fesi.flowit.user.web.response.UserSignedUpResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

interface UserApiSpec {

    @Operation(
        summary = "사용자 회원가입",
        description = """
            [POST] http://IP:PORT/users
            {
                "email": "user@example.com",
                "password": "password123",
                "name": "홍길동"
            }
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "회원가입 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 데이터",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )]
            ),
            ApiResponse(
                responseCode = "409",
                description = "이미 존재하는 사용자",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )]
            )
        ]
    )
    @PostMapping("/users")
    fun signUp(@RequestBody userRequest: UserRequest): ResponseEntity<ApiResult<UserResponse>>

    @Operation(
        summary = "이메일 회원가입 여부 확인",
        description = """
            [GET] http://IP:PORT/users?email=user@example.com
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "사용자 회원가입 여부 (result.exists 값으로 구분)",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserSignedUpResponse::class),
                    examples = [ExampleObject(
                        summary = "notRegistered",
                        name = "해당 이메일로 회원가입한 사용자가 없습니다",
                        value = """{
                        "code": "0000",
                        "message": "OK", 
                        "result": {
                            "exists": false
                        }
                    }"""
                    ), ExampleObject(
                        summary = "alreadyRegistered",
                        name = "해당 이메일로 회원가입한 사용자가 이미 있습니다",
                        value = """{
                        "code": "0000",
                        "message": "OK",
                        "result": {
                            "exists": true
                        }
                    }""")]
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 데이터",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiResult.Exception::class)
                )]
            )
        ]
    )
    fun hasSignedUp(@RequestParam email: String): ResponseEntity<ApiResult<UserSignedUpResponse>>
}