package com.arya.bookmydoc.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arya.bookmydoc.domain.model.UserRole

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val age: Int?,
    val gender: String,
    val profileImageUrl: String?,
    val role: String,
    val appointments: List<String>
)
