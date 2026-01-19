package com.arya.bookmydoc.data.dto



data class UserDto(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val profileImageUrl: String? = null,
    val role: String? = null,
    val appointments: List<String>? = null
)
