package com.wooteco.checkin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CheckInApplication

fun main(args: Array<String>) {
    runApplication<CheckInApplication>(*args)
}
