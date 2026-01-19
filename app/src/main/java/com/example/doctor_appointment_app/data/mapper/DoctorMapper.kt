package com.arya.bookmydoc.data.mapper

import com.arya.bookmydoc.data.dto.DoctorDto
import com.arya.bookmydoc.domain.model.DoctorContactDetails
import com.arya.bookmydoc.domain.model.Doctor
import com.arya.bookmydoc.domain.model.UserRole

fun DoctorDto.toDomain(): Doctor {
    return Doctor(
        id = id?:"",
        name = name?:"",
        role = when(role){
            "doctor" -> UserRole.DOCTOR
            "admin" -> UserRole.ADMIN
            else -> UserRole.PATIENT
        },
        profileImage = profileImage?:"",
        biography = biography?:"",
        experience = experience?:0,
        specialization = specialization?:"",
        address = address?:"",
        location = location?:"",
        phoneNumber = phoneNumber?:"",
        rating = rating?:0.0,
        totalPatients = totalPatients ?: 0,
        consultationFee = consultationFee ?: 0.0,
        available = available ?: false,
        site = site,
        appointments = appointments?:emptyList()
    )
}

fun Doctor.toDto(): DoctorDto {
    return DoctorDto(
        id = id,
        name = name,
        role = role.name.lowercase(),
        profileImage = profileImage,
        biography = biography,
        experience = experience,
        specialization = specialization,
        address = address,
        location = location,
        phoneNumber = phoneNumber,
        rating = rating,
        totalPatients = totalPatients,
        consultationFee = consultationFee,
        available = available,
        site = site,
        appointments = appointments
    )
}

fun Doctor.toDoctorDetails(): DoctorContactDetails{
    return DoctorContactDetails(
        name = name,
        mobile = phoneNumber,
        address = address,
        site = site
    )
}