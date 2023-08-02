package com.wooteco.checkin.controller

import com.wooteco.checkin.service.AttendanceService
import com.wooteco.checkin.service.dto.AttendanceRequest
import com.wooteco.checkin.service.dto.AttendanceResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/attendance")
class AttendanceController(private val attendanceService: AttendanceService) {

    @PostMapping("/check-in")
    fun checkIn(@RequestBody attendanceRequest: AttendanceRequest): ResponseEntity<Void> {
        attendanceService.checkIn(attendanceRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/check-out")
    fun checkOut(@RequestBody attendanceRequest: AttendanceRequest): ResponseEntity<Void> {
        attendanceService.checkOut(attendanceRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun getAttendance(@DateTimeFormat(pattern = "yyyy-MM-dd") date: LocalDate): ResponseEntity<List<AttendanceResponse>> {
        return ResponseEntity.ok(attendanceService.getAttendance(date))
    }
}
