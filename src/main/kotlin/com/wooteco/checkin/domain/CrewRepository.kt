package com.wooteco.checkin.domain

import org.springframework.data.jpa.repository.JpaRepository

interface CrewRepository : JpaRepository<Crew, Long> {

    fun findByNickname(nickname: String): Crew?
}
