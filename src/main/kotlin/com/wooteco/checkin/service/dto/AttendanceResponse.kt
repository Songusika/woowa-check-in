package com.wooteco.checkin.service.dto

import com.wooteco.checkin.domain.Attendance
import java.time.LocalDateTime

data class AttendanceResponse(
        val id: Long?,
        val nickname: String,
        val status: String,
        val checkInTime: LocalDateTime,
        val checkOutTime: LocalDateTime?
) {
    companion object {
        fun from(attendance: Attendance): AttendanceResponse {
            return AttendanceResponse(
                    attendance.getId(),
                    attendance.getNickname(),
                    attendance.getStatus(),
                    attendance.getCheckInTime(),
                    attendance.getCheckOutTime()
            )
        }
    }
}
