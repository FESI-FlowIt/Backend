package com.fesi.flowit.user.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserControllerAdvice {
    @ExceptionHandler(UserAlreadySignedUpException::class)
    fun handleUserAlreadySignedUpException(
        userAlreadySignedUpException: UserAlreadySignedUpException
    ): ResponseEntity<Void> {
        return ResponseEntity.badRequest().build()
    }
}
