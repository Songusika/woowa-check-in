package com.wooteco.checkin.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AttendanceRepository : JpaRepository<Attendance, Long> {
    fun existsByCrewAndDate(crew: Crew, date: LocalDate): Boolean

    fun findByCrewAndDate(crew: Crew, date: LocalDate): Attendance?

    fun findAllByDate(date: LocalDate): List<Attendance>
}
