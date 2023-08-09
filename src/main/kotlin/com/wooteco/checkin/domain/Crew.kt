package com.wooteco.checkin.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Crew(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0L,
    private var nickname: String,
) {
    constructor(nickname: String) : this(0L, nickname)

    fun getNickname(): String {
        return nickname
    }
}
