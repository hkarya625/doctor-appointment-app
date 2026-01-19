package com.arya.bookmydoc.domain.model


import java.time.LocalDateTime

data class Appointment(
    val id: String,
    val doctorId: String,
    val patientId: String,
    val doctorName: String,
    val dateTime: LocalDateTime,
    val status: AppointmentStatus
)

