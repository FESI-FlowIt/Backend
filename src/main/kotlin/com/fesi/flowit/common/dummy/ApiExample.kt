package com.fesi.flowit.common.dummy

import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class DummyDto(
    val name: String,
    val age: Int
)

data class DummyResDto(
    val name: String,
    val age: Int,
    val msg: String
)

@RestController
class ApiExample {

    @GetMapping("/test/{id}")
    @Operation(
        summary = "GET 요청 예시",
        description = "[GET] http://IP:PORT/test"
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "요청 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )]
            )
        ]
    )
    fun getExample(@PathVariable id: Int): ResponseEntity<ApiResult<String>> {
        return ApiResponse.ok("GET > $id")
    }

    @PostMapping("/test")
    @Operation(
        summary = "POST 요청 예시",
        description =
            """
                [POST] http://IP:PORT/test 
                {
                    "name" : "james",
                    "age"  :  20
                }
            """
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "요청 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )]
            )
        ]
    )
    fun postExample(@RequestBody request: DummyDto): ResponseEntity<ApiResult<DummyResDto>> {
        val result = DummyResDto(request.name, request.age, "API Response")

        return ApiResponse.created(result)
    }
}