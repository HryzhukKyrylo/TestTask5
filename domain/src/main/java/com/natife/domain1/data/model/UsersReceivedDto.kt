package com.natife.domain1.data.model


data class UsersReceivedDto(val users: List<User>) : Payload


data class User(val id: String, val name: String)
