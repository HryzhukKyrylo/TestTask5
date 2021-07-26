package com.natife.core.data.model

data class MessageDto(val from: User, val message: String, var time: String) : Payload
