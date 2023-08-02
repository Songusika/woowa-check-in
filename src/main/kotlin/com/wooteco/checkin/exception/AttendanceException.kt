package com.wooteco.checkin.exception

import org.springframework.http.HttpStatus

open class AttendanceException(message: String, httpStatus: HttpStatus) : BaseException(message, httpStatus) {

    class AlreadyCheckInException : AttendanceException("이미 출석을 하였습니다.", HttpStatus.BAD_REQUEST)
    class NotCheckInException : AttendanceException("출석을 하지 않은 상태라 하교가 불가능합니다.", HttpStatus.BAD_REQUEST)
}
