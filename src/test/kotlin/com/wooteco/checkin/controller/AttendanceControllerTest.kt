package com.wooteco.checkin.controller

import com.wooteco.checkin.domain.Crew
import com.wooteco.checkin.domain.CrewRepository
import com.wooteco.checkin.service.dto.AttendanceRequest
import com.wooteco.checkin.service.dto.AttendanceResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    @Nested
    inner class 크루는_출석시 {

        @Test
        fun `성공하면 201 응답을 받는다`() {
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
        fun `두번 출석 시 400 에러를 반환한다`() {
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
        fun `존재하지 않는 크루면 404 에러를 반환한다`() {
            // given
            val nickName = "블랙캣"
            val checkInRequest = AttendanceRequest(nickName)

            // when
            val response = 출석한다(checkInRequest)

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
        }

    }


    @Nested
    inner class 크루는_하교시 {

        @Test
        fun `성공하면 201 응답을 반환한다`() {
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
        fun `크루가 없으면 404 에러를 반환한다`() {
            // given
            val nickName = "은서"
            val request = AttendanceRequest(nickName)

            // when
            val response = 하교한다(request)

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
        }

        @Test
        fun `출석하지 않은 상태로 하교 요청시 400 에러를 반환한다`() {
            // given
            val nickName = "민규"
            crewRepository.save(Crew(nickName))
            val request = AttendanceRequest(nickName)

            // when
            val response = 하교한다(request)

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        }
    }

    @Test
    fun `크루의 출석 상태를 반환한다`() {
        // given
        val nickName = "현서"
        crewRepository.save(Crew(nickName))
        val request = AttendanceRequest(nickName)
        출석한다(request)
        하교한다(request)

        // when
        val response = 조회한다(LocalDate.now())

        // then
        val result: List<AttendanceResponse> = response.`as`(
                object : ParameterizedTypeReference<List<AttendanceResponse?>?>() {}.type
        )
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(result).hasSize(1)
    }

    private fun 출석한다(checkInRequest: AttendanceRequest): ExtractableResponse<Response> =
            RestAssured
                    .given().log().all()
                    .body(checkInRequest).contentType(ContentType.JSON)
                    .`when`().post("/attendance/check-in")
                    .then().extract()

    private fun 하교한다(request: AttendanceRequest): ExtractableResponse<Response> =
            RestAssured
                    .given().log().all()
                    .body(request).contentType(ContentType.JSON)
                    .`when`().post("/attendance/check-out")
                    .then().extract()

    private fun 조회한다(date: LocalDate): ExtractableResponse<Response> =
            RestAssured
                    .given().log().all()
                    .`when`().get("/attendance?date=" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .then().log().all()
                    .extract()
}
