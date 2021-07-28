package com.natife.domain1.data.model

data class SendMessageDto(
    val id: String,
    val receiver: String,
    val message: String,
    var time: String
) : Payload
