package com.wooteco.checkin.service

import com.wooteco.checkin.domain.Attendance
import com.wooteco.checkin.domain.AttendanceRepository
import com.wooteco.checkin.domain.CrewRepository
import com.wooteco.checkin.exception.AttendanceException
import com.wooteco.checkin.exception.CrewException
import com.wooteco.checkin.service.dto.AttendanceRequest
import org.springframework.stereotype.Service

@Service
class AttendanceService(
    private val crewRepository: CrewRepository,
    private val attendanceRepository: AttendanceRepository,
) {

    fun checkIn(attendanceRequest: AttendanceRequest): Long {
        val crew = crewRepository.findByNickname(attendanceRequest.nickname)
            ?: throw CrewException.NotFoundException()
        val attendance = Attendance(crew)

        if (attendanceRepository.existsByCrewAndDate(crew, attendance.getDate())) {
            throw AttendanceException.AlreadyCheckInException()
        }

        attendanceRepository.save(attendance)
        return attendance.getId()!!
    }
}
