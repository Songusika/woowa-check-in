package com.wooteco.checkin.controller

import com.wooteco.checkin.domain.Crew
import com.wooteco.checkin.domain.CrewRepository
import com.wooteco.checkin.service.dto.AttendanceRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql("/truncated.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AttendanceControllerTest(
    @LocalServerPort
    private val port: Int,
) {

    @Autowired
    private lateinit var crewRepository: CrewRepository

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @Test
    fun `크루는 정상 출석 시 201 응답을 받는다`() {
        // given
        val nickName = "블랙캣"
        crewRepository.save(Crew(nickName))
        val checkInRequest = AttendanceRequest(nickName)

        // when
        val response = RestAssured
            .given().log().all()
            .body(checkInRequest).contentType(ContentType.JSON)
            .`when`().post("/attendance/check-in").then().extract()

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `크루는 두번 출석 시 404 에러를 반환한다`() {
        // given
        val nickName = "블랙캣"
        crewRepository.save(Crew(nickName))
        val checkInRequest = AttendanceRequest(nickName)
        RestAssured
            .given().log().all()
            .body(checkInRequest).contentType(ContentType.JSON)
            .`when`().post("/attendance/check-in").then().extract()

        // when
        val response = RestAssured
            .given().log().all()
            .body(checkInRequest).contentType(ContentType.JSON)
            .`when`().post("/attendance/check-in").then().extract()

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `없는 크루는 출석 시 404 에러를 반환한다`() {
        // given
        val nickName = "블랙캣"
        val checkInRequest = AttendanceRequest(nickName)

        // when
        val response = RestAssured
            .given().log().all()
            .body(checkInRequest).contentType(ContentType.JSON)
            .`when`().post("/attendance/check-in").then().extract()

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }
}
