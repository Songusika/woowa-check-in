package com.wooteco.checkin.domain

import java.time.LocalDateTime

enum class AttendanceStatus(val status: String) {
    UNREGISTERED("미등록"),
    ATTENDANCE("출석"),
    LATE("지각"),
    ABSENT("결석"),
    ;

    companion object {
        fun from(time: LocalDateTime): AttendanceStatus {
            // TODO: 지각 정책 결정되면 하기
            return ATTENDANCE
        }
    }
}
