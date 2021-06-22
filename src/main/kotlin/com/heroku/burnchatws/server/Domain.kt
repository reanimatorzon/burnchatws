package com.heroku.burnchatws.server

import java.time.LocalDateTime

data class Message(
    val from: String,
    val to: String,
    val text: String
)

data class OutputMessage(
    val from: String,
    val to: String,
    val text: String,
    val time: LocalDateTime
)