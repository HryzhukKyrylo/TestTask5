package com.natife.testtask5.data.model

data class SendMessageDto(
    val id: String,
    val receiver: String,
    val message: String,
    var time: String
) : Payload
