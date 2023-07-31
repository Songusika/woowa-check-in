package com.wooteco.checkin.controller

import com.wooteco.checkin.exception.BaseException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleException(baseException: BaseException): ResponseEntity<String> {
        return ResponseEntity.status(baseException.httpStatus).body(baseException.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(): ResponseEntity<String> {
        return ResponseEntity.internalServerError().body("예상치 못한 예외가 발생했습니다.")
    }
}
