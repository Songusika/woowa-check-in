package com.wooteco.checkin.service

import com.wooteco.checkin.domain.Attendance
import com.wooteco.checkin.domain.AttendanceRepository
import com.wooteco.checkin.domain.CrewRepository
import com.wooteco.checkin.exception.AttendanceException
import com.wooteco.checkin.exception.CrewException
import com.wooteco.checkin.service.dto.AttendanceRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AttendanceService(
    private val crewRepository: CrewRepository,
    private val attendanceRepository: AttendanceRepository,
) {

    fun checkIn(attendanceRequest: AttendanceRequest): Long {
        val crew = getCrew(attendanceRequest)
        val attendance = Attendance(crew)

        if (attendanceRepository.existsByCrewAndDate(crew, attendance.getDate())) {
            throw AttendanceException.AlreadyCheckInException()
        }

        attendanceRepository.save(attendance)
        return attendance.getId()!!
    }

    fun checkOut(attendanceRequest: AttendanceRequest) {
        val crew = getCrew(attendanceRequest)
        val now = LocalDateTime.now()
        val attendance =  attendanceRepository.findByCrewAndDate(crew, now.toLocalDate())
                ?: throw AttendanceException.NotCheckInException()
        attendance.checkOut(now)
    }

    private fun getCrew(attendanceRequest: AttendanceRequest) =
            crewRepository.findByNickname(attendanceRequest.nickname)
                    ?: throw CrewException.NotFoundException()
}
