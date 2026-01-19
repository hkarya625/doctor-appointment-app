package com.arya.bookmydoc.domain.usecase.appointment

import com.arya.bookmydoc.domain.model.Appointment
import com.arya.bookmydoc.domain.repository.AppointmentRepository
import com.arya.bookmydoc.domain.repository.DoctorRepository
import javax.inject.Inject

class BookAppointmentUseCase@Inject constructor(
    private val appointmentRepository: AppointmentRepository
) {
    suspend operator fun invoke(appointment: Appointment) = appointmentRepository.bookAppointment(appointment)
}