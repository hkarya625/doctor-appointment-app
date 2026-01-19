package com.arya.bookmydoc.data.repository

import com.arya.bookmydoc.data.remote.FirebaseDataSource
import com.arya.bookmydoc.domain.model.Appointment
import com.arya.bookmydoc.domain.repository.AppointmentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
): AppointmentRepository {
    override suspend fun getAppointments(appointments: List<String>): Result<List<Appointment>> {
        return firebaseDataSource.getAppointments(appointments)
    }

    override suspend fun bookAppointment(appointment: Appointment): Result<Boolean> {
        return firebaseDataSource.bookAppointment(appointment)
    }
}