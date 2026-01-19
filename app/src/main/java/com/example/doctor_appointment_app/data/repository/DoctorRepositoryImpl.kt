package com.arya.bookmydoc.data.repository

import com.arya.bookmydoc.data.remote.FirebaseDataSource
import com.arya.bookmydoc.domain.model.Doctor
import com.arya.bookmydoc.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DoctorRepositoryImpl@Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
): DoctorRepository {
    override suspend fun fetchDoctor(): Result<List<Doctor>> {
        return firebaseDataSource.fetchDoctor()
    }
}