package com.arya.bookmydoc.domain.repository

import com.arya.bookmydoc.domain.model.Doctor
import kotlinx.coroutines.flow.Flow

interface DoctorRepository {
    suspend fun fetchDoctor(): Result<List<Doctor>>
}