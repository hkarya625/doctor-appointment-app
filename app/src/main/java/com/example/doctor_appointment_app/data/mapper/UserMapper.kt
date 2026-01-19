package com.arya.bookmydoc.data.mapper

import androidx.compose.ui.text.toLowerCase
import com.arya.bookmydoc.data.dto.UserDto
import com.arya.bookmydoc.data.local.entity.UserEntity
import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.domain.model.UserRole
import java.util.Locale
import java.util.Locale.getDefault

fun UserDto.toDomain(): User {
    return User(
        id = id?:"",
        name = name?:"",
        email = email?:"",
        phoneNumber = phoneNumber?:"",
        age = age,
        gender = gender?:"",
        profileImageUrl = profileImageUrl,
        role = when(role){
            "doctor"-> UserRole.DOCTOR
            "admin" -> UserRole.ADMIN
            else -> UserRole.PATIENT
        },
        appointments = appointments?:emptyList()
    )
}
fun User.toDto(): UserDto{
    return UserDto(
        id = id,
        name = name,
        email = email.trim(),
        phoneNumber = phoneNumber,
        age = age,
        gender = gender,
        profileImageUrl = profileImageUrl,
        role = role.name.lowercase(),
        appointments = appointments
    )
}

fun UserEntity.toDomain() = User(
    id = uid,
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    age = age,
    gender = gender,
    profileImageUrl = profileImageUrl,
    role =  when(role){
        ""-> UserRole.PATIENT
        else -> UserRole.DOCTOR
    },
    appointments = appointments
)
fun User.toEntity() = UserEntity(
    uid = id,
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    age = age,
    gender = gender,
    profileImageUrl = profileImageUrl,
    role = role.toString().lowercase(),
    appointments = appointments
)

