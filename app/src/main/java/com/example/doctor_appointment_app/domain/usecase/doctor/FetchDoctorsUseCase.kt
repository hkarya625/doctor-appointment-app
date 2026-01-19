package com.arya.bookmydoc.domain.usecase.doctor

import com.arya.bookmydoc.domain.model.Doctor
import com.arya.bookmydoc.domain.repository.DoctorRepository
import com.arya.bookmydoc.domain.repository.UserRepository
import javax.inject.Inject

class FetchDoctorsUseCase@Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    suspend operator fun invoke():Result<List<Doctor>> = doctorRepository.fetchDoctor()

}