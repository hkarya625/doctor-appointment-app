package com.arya.bookmydoc.di


import com.arya.bookmydoc.data.repository.AppointmentRepositoryImpl
import com.arya.bookmydoc.data.repository.CategoryRepositoryImpl
import com.arya.bookmydoc.data.repository.DoctorRepositoryImpl
import com.arya.bookmydoc.data.repository.RoomRepositoryImpl
import com.arya.bookmydoc.data.repository.UserRepositoryImpl
import com.arya.bookmydoc.domain.repository.AppointmentRepository
import com.arya.bookmydoc.domain.repository.CategoryRepository
import com.arya.bookmydoc.domain.repository.DoctorRepository
import com.arya.bookmydoc.domain.repository.RoomRepository
import com.arya.bookmydoc.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun getUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun getDoctorRepository(
        doctorRepositoryImpl: DoctorRepositoryImpl
    ): DoctorRepository

    @Binds
    @Singleton
    abstract fun getCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun getAppointmentRepository(
        appointmentRepositoryImpl: AppointmentRepositoryImpl
    ): AppointmentRepository

    @Binds
    @Singleton
    abstract fun getRoomRepository(
        roomRepositoryImpl: RoomRepositoryImpl
    ): RoomRepository
}