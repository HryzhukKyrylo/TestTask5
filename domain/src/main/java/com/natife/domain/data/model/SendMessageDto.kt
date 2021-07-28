package com.natife.domain.data.model

data class SendMessageDto(
    val id: String,
    val receiver: String,
    val message: String,
    var time: String
) : Payload
