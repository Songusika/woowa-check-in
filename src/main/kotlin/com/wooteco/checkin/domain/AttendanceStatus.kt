package com.wooteco.checkin.domain

import java.time.LocalDateTime

enum class AttendanceStatus(val status: String) {
    ATTENDANCE("출석"),
    LATE("지각"),
    ;

    companion object {
        fun from(time: LocalDateTime): AttendanceStatus {
            // TODO: 지각 정책 결정되면 하기
            return ATTENDANCE
        }
    }
}
