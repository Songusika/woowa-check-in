package com.wooteco.checkin.exception

import org.springframework.http.HttpStatus

open class CrewException(message: String, httpStatus: HttpStatus) : BaseException(message, httpStatus) {

    class NotFoundException : CrewException("크루 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST)
}
