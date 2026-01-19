package com.arya.bookmydoc.domain.usecase.appointment

import com.arya.bookmydoc.domain.repository.AppointmentRepository
import com.arya.bookmydoc.domain.repository.DoctorRepository
import javax.inject.Inject

class GetAppointmentsUseCase@Inject constructor(
    private val appointmentRepository: AppointmentRepository
) {
    suspend operator fun invoke(appointments:List<String>) = appointmentRepository.getAppointments(appointments)
}