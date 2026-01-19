package com.arya.bookmydoc.data.mapper

import com.arya.bookmydoc.data.dto.AppointmentDto
import com.arya.bookmydoc.domain.model.Appointment
import com.arya.bookmydoc.domain.model.AppointmentStatus
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


private fun Timestamp.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochSecond(this.seconds, this.nanoseconds.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

private fun LocalDateTime.toTimestamp(): Timestamp {
    val instant = this.atZone(ZoneId.systemDefault()).toInstant()
    return Timestamp(instant.epochSecond, instant.nano)
}


fun AppointmentDto.toDomain(): Appointment {
    return Appointment(
        id = id ?: "",
        doctorId = doctorId ?: "",
        patientId = patientId ?: "",
        doctorName = doctorName ?: "",
        dateTime = dateTime?.toLocalDateTime() ?: LocalDateTime.now(),
        status = when(status){
            "pending"-> AppointmentStatus.PENDING
            "confirmed"-> AppointmentStatus.CONFIRMED
            "completed"-> AppointmentStatus.COMPLETED
            else -> AppointmentStatus.CANCELLED
        }
    )
}

fun Appointment.toDto(): AppointmentDto {
    return AppointmentDto(
        id = id,
        doctorId = doctorId,
        patientId = patientId,
        doctorName = doctorName,
        dateTime = dateTime.toTimestamp(),
        status = status.name.lowercase()
    )
}