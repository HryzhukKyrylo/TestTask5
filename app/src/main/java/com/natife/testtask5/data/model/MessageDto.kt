package com.natife.testtask5.data.model

data class MessageDto(val from: User, val message: String, var time: String) : Payload
