package com.wooteco.checkin.domain

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Attendance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "crew_id")
    private var crew: Crew,
    @Column(nullable = false)
    private var date: LocalDate,
    @Enumerated(value = EnumType.STRING)
    private var status: AttendanceStatus,
    @Column(nullable = false)
    private var checkInTime: LocalDateTime,
    private var checkOutTime: LocalDateTime?,
) {
    constructor(crew: Crew) : this(
        null,
        crew,
        LocalDate.now(),
        AttendanceStatus.from(LocalDateTime.now()),
        LocalDateTime.now(),
        null,
    )

    fun checkOut(time: LocalDateTime) {
        checkOutTime = time
    }

    fun getDate(): LocalDate {
        return date
    }

    fun getId(): Long? {
        return id
    }

    fun getNickname(): String {
        return crew.getNickname()
    }

    fun getStatus(): String {
        return status.status
    }

    fun getCheckInTime(): LocalDateTime {
        return checkInTime
    }

    fun getCheckOutTime(): LocalDateTime? {
        return checkOutTime
    }
}
