package com.wooteco.checkin.controller

import com.wooteco.checkin.service.AttendanceService
import com.wooteco.checkin.service.dto.AttendanceRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/attendance")
class AttendanceController(private val attendanceService: AttendanceService) {

    @PostMapping("/check-in")
    fun checkIn(@RequestBody attendanceRequest: AttendanceRequest): ResponseEntity<Void> {
        attendanceService.checkIn(attendanceRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/check-out")
    fun checkOugt(@RequestBody attendanceRequest: AttendanceRequest): ResponseEntity<Void> {
        attendanceService.checkOut(attendanceRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
