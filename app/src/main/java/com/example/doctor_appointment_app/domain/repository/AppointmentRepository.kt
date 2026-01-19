package com.arya.bookmydoc.domain.repository

import com.arya.bookmydoc.domain.model.Appointment

interface AppointmentRepository{
    suspend fun getAppointments(appointments: List<String>): Result<List<Appointment>>
    suspend fun bookAppointment(appointment: Appointment): Result<Boolean>
}