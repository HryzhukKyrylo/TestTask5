package com.natife.domain1.data.model

data class MessageDto(val from: User, val message: String, var time: String) : Payload
