package com.arya.bookmydoc.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.bookmydoc.domain.model.Appointment
import com.arya.bookmydoc.domain.model.AppointmentStatus
import com.arya.bookmydoc.domain.usecase.appointment.BookAppointmentUseCase
import com.arya.bookmydoc.domain.usecase.appointment.GetAppointmentsUseCase
import com.arya.bookmydoc.domain.usecase.auth.GetUserRemoteUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val bookAppointmentUseCase: BookAppointmentUseCase,
    private val getAppointmentsUseCase: GetAppointmentsUseCase,
    private val getUserRemoteUseCase: GetUserRemoteUseCase
): ViewModel() {
    private val _appointmentsUiState = MutableStateFlow(AppointmentUiState())
    val appointmentUiState: StateFlow<AppointmentUiState> = _appointmentsUiState

    private val _bookingUiState = MutableStateFlow(BookingUiState())
    val bookingUiState: StateFlow<BookingUiState> = _bookingUiState


    fun bookAppointment(
        doctorId:String,
        doctorName:String,
        date: LocalDate,
        time: LocalTime){
        viewModelScope.launch {
            _bookingUiState.update { state ->
                state.copy(
                    isLoading = true,
                    isBooked = false,
                    error = null,
                    showDialog = true
                )
            }
            val appointment = Appointment(
                id = "",
                doctorId = doctorId,
                patientId = "",
                doctorName = doctorName,
                dateTime = LocalDateTime.of(date, time),
                status = AppointmentStatus.PENDING
            )
            val result = bookAppointmentUseCase.invoke(appointment)
            result.fold(
                onSuccess = {
                    _bookingUiState.update {
                        it.copy(
                            isLoading = false,
                            isBooked = true,
                            showDialog = true
                        )
                    }
                },
                onFailure = { exception ->
                    _bookingUiState.update {
                        it.copy(
                            isLoading = false,
                            isBooked = false,
                            error = exception.message,
                            showDialog = false
                        )
                    }
                }
            )

        }
    }


    fun loadAppointments(){
        viewModelScope.launch {
            _appointmentsUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val userId = FirebaseAuth.getInstance().uid
            val user = userId?.let { getUserRemoteUseCase.invoke(userId) }
            user?.onSuccess {
                val appointments = getAppointmentsUseCase.invoke(it.appointments)
                appointments.fold(
                    onSuccess = { appointments->
                        _appointmentsUiState.update {
                            it.copy(
                                isLoading = false,
                                appointments = appointments
                            )
                        }
                    },
                    onFailure = { exception->
                        _appointmentsUiState.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message
                            )
                        }
                    }
                )
            }
        }
    }

    fun retryAppointments(){
//        loadDoctor()
    }

    fun dismissDialog() {
        _bookingUiState.update {
            it.copy(
                showDialog = false,
                isBooked = false,
                isLoading = false
            )
        }
    }
}

data class AppointmentUiState(
    val isLoading: Boolean = false,
    val appointments:List<Appointment> = emptyList(),
    val error:String? = null
)
data class BookingUiState(
    val isLoading: Boolean = false,
    val isBooked:Boolean = false,
    val error:String? = null,
    val showDialog:Boolean = false
)