package com.arya.bookmydoc.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val age: Int?,
    val gender: String,
    val profileImageUrl: String?,
    val role: UserRole,
    val appointments: List<String>
)