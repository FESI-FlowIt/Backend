package com.fesi.flowit.user.controller

import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.user.dto.SignUpRequestDto
import com.fesi.flowit.user.dto.SignUpResponseDto
import com.fesi.flowit.user.dto.UserExistCheckResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletRequest
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
                    schema = Schema(implementation = SignUpResponseDto::class)
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
    fun signUp(@RequestBody userRequest: SignUpRequestDto): ResponseEntity<ApiResult<SignUpResponseDto>>

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
                    schema = Schema(implementation = UserExistCheckResponseDto::class),
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
    fun hasSignedUp(@RequestParam email: String): ResponseEntity<ApiResult<UserExistCheckResponseDto>>

    @Operation(
        summary = "사용자 정보 조회",
        description = """
            [GET] http://IP:PORT/users/me
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "회원 정보 조회",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SignUpResponseDto::class),
                    examples = [ExampleObject(
                        summary = "userInfo",
                        name = "정상적으로 회원 정보를 조회한 경우",
                        value = """{
                            "code": "0000",
                            "message": "OK",
                            "result": {
                              "id": 1,
                              "email": "user@example.com",
                              "name": "홍길동",
                              "createdAt": "2025-07-26T13:43:24.893056",
                              "updatedAt": "2025-07-26T13:43:24.893073"
                            }
                        }"""
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 데이터",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiResult.Exception::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiResult.Exception::class),
                    examples = [ExampleObject(
                        summary = "tokenExpired",
                        name = "액세스 토큰이 만료된 경우",
                        value = """{
                            "code": "0000",
                            "message": "Token has expired",
                        }"""
                    )]
                )]
            ),
        ]
    )
    fun getUserInfo(request: HttpServletRequest): ResponseEntity<ApiResult<SignUpResponseDto>>
}