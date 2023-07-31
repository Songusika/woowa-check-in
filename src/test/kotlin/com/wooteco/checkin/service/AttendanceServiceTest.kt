package com.wooteco.checkin.service

import com.wooteco.checkin.domain.AttendanceRepository
import com.wooteco.checkin.domain.Crew
import com.wooteco.checkin.domain.CrewRepository
import com.wooteco.checkin.exception.AttendanceException
import com.wooteco.checkin.exception.CrewException
import com.wooteco.checkin.service.dto.AttendanceRequest
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class AttendanceServiceTest {

    @Autowired
    private lateinit var crewRepository: CrewRepository

    @Autowired
    private lateinit var attendanceRepository: AttendanceRepository

    @Autowired
    private lateinit var attendanceService: AttendanceService

    @Test
    fun `출석시 크루가 존재하지 않으면 예외를 발생한다`() {
        assertThatThrownBy() { attendanceService.checkIn(AttendanceRequest("블랙캣")) }.isInstanceOf(
            CrewException.NotFoundException::class.java,
        )
    }

    @Test
    fun `존재하는 크루는 출석할 수 있다`() {
        // given
        val nickName = "오도"
        val crew = Crew(nickName)
        crewRepository.save(crew)

        // when
        val checkInId = attendanceService.checkIn(AttendanceRequest(nickName))

        // then
        val attendance = attendanceRepository.findById(checkInId)
        assertThat(attendance).isNotNull
    }

    @Test
    fun `이미 출석한 크루가 출석하면 예외가 발생한다`() {
        // given
        val nickName = "오도"
        val crew = Crew(nickName)
        crewRepository.save(crew)
        attendanceService.checkIn(AttendanceRequest(nickName))

        // expect
        assertThatThrownBy() { attendanceService.checkIn(AttendanceRequest(nickName)) }.isInstanceOf(
            AttendanceException.AlreadyCheckInException::class.java,
        )
    }
}
