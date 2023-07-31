package com.wooteco.checkin.exception

import org.springframework.http.HttpStatus

open class AttendanceException(message: String, httpStatus: HttpStatus) : BaseException(message, httpStatus) {

    class AlreadyCheckInException : AttendanceException("이미 출석을 하였습니다.", HttpStatus.BAD_REQUEST)
}
