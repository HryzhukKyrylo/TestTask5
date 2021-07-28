package com.natife.domain.data.model

data class MessageDto(val from: User, val message: String, var time: String) : Payload
