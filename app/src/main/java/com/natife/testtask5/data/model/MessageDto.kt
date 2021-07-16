package com.natife.testtask5.data.model

import model.MessageItem
import model.Payload
import model.User

data class MessageDto(val from: User, val message: String) : Payload, MessageItem()
