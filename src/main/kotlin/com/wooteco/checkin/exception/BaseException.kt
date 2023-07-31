package com.wooteco.checkin.exception

import org.springframework.http.HttpStatus

open class BaseException(message: String, val httpStatus: HttpStatus) : RuntimeException(message)
