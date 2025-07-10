package com.fesi.flowit.common.dummy

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
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

@RestController
class ApiExample {

    @GetMapping("/test/{id}")
    @Operation(
        summary = "GET 요청 예시",
        description = "[GET] http://IP:PORT/test"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "요청 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )]
            )
        ]
    )
    fun getExample(@PathVariable id: Int): ResponseEntity<String> {
        return ResponseEntity.ok("GET > $id")
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
            ApiResponse(
                responseCode = "200",
                description = "요청 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )]
            )
        ]
    )
    fun postExample(@RequestBody request: DummyDto): ResponseEntity<String> {
        return ResponseEntity.ok("POST > name: $request.name, age: $request.age")
    }

}