package com.arya.bookmydoc.data.dto

import com.google.firebase.Timestamp

data class AppointmentDto(
    val id: String? = null,
    val doctorId: String? = null,
    val patientId: String? = null,
    val doctorName: String? = null,
    val dateTime: Timestamp? = null,
    val status: String? = null
)
