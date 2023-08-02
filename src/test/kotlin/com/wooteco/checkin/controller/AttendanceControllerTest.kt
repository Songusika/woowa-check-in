package com.wooteco.checkin.controller

import com.wooteco.checkin.domain.Crew
import com.wooteco.checkin.domain.CrewRepository
import com.wooteco.checkin.service.dto.AttendanceRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Suppress("NonAsciiCharacters")
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
        val response = 출석한다(checkInRequest)

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `크루는 두번 출석 시 400 에러를 반환한다`() {
        // given
        val nickName = "블랙캣"
        crewRepository.save(Crew(nickName))
        val checkInRequest = AttendanceRequest(nickName)
        출석한다(checkInRequest)

        // when
        val response = 출석한다(checkInRequest)

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `없는 크루는 출석 시 404 에러를 반환한다`() {
        // given
        val nickName = "블랙캣"
        val checkInRequest = AttendanceRequest(nickName)

        // when
        val response = 출석한다(checkInRequest)

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `크루가 하교에 성공하면 201 응답을 반환한다`() {
        // given
        val nickName = "현서"
        crewRepository.save(Crew(nickName))
        val request = AttendanceRequest(nickName)
        출석한다(request)

        // when
        val response = 하교한다(request)

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `하교시 크루가 없으면 404 에러를 반환한다`() {
        // given
        val nickName = "은서"
        val request = AttendanceRequest(nickName)

        // when
        val response = 하교한다(request)

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `크루가 출석하지 않은 상태로 하교 요청시 400 에러를 반환한다`() {
        // given
        val nickName = "민규"
        crewRepository.save(Crew(nickName))
        val request = AttendanceRequest(nickName)

        // when
        val response = 하교한다(request)

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    private fun 출석한다(checkInRequest: AttendanceRequest): ExtractableResponse<Response> =
            RestAssured
                    .given().log().all()
                    .body(checkInRequest).contentType(ContentType.JSON)
                    .`when`().post("/attendance/check-in").then().extract()

    private fun 하교한다(request: AttendanceRequest): ExtractableResponse<Response> =
            RestAssured
                    .given().log().all()
                    .body(request).contentType(ContentType.JSON)
                    .`when`().post("/attendance/check-out").then().extract()
}
