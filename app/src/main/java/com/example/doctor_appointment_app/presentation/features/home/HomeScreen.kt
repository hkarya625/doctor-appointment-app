package com.arya.bookmydoc.presentation.features.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arya.bookmydoc.R
import com.arya.bookmydoc.domain.model.Appointment
import com.arya.bookmydoc.domain.model.Category
import com.arya.bookmydoc.domain.model.Doctor
import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.presentation.viewmodel.AppointmentViewModel

import com.arya.bookmydoc.presentation.viewmodel.HomeViewModel
import com.arya.bookmydoc.presentation.viewmodel.UserViewmodel
import com.google.firebase.auth.FirebaseUser

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    userViewmodel: UserViewmodel = hiltViewModel(),
    appointmentViewModel: AppointmentViewModel = hiltViewModel(),
    onDoctorDetails:(String)-> Unit,
    onAllDoctor:()-> Unit
){

    LaunchedEffect(Unit) {
        userViewmodel.loadCurrentUser()
    }


    val categoryState by homeViewModel.categoryUiState.collectAsStateWithLifecycle()
    val doctorState by homeViewModel.doctorUiState.collectAsStateWithLifecycle()
    val userState by userViewmodel.profileUiState.collectAsStateWithLifecycle()

    val categoryList = categoryState.categories
    val isCategoryLoading = categoryState.isLoading
    val categoryError = categoryState.error

    val doctorList = doctorState.doctors
    val isDoctorLoading = doctorState.isLoading
    val doctorError = doctorState.error

    val appointmentList = emptyList<Appointment>()
    val isAppointmentLoading = false
    val appointmentError = null

    val user = userState.user

    Scaffold(
        topBar = {
            HomeHeader(
                name = user?.name ?: "",
                photo = user?.profileImageUrl,
                onProfileClick = {},
                onBellClick = {},
                onSearchClick = {}
            ) { }
        }
    ) { paddingValues ->

        Log.d("user", user.toString())
        HomeContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            categories = categoryList,
            isCategoryLoading = isCategoryLoading,
            categoryError = categoryError,

            doctors = doctorList,
            isDoctorLoading = isDoctorLoading,
            doctorError = doctorError,

            appointments = appointmentList,
            isAppointmentLoading = isAppointmentLoading,
            appointmentError = appointmentError,


            onRetryCategories = {
                homeViewModel.retryCategories()
            },
            onRetryDoctors = {
                homeViewModel.retryDoctors()
            },
            onRetryAppointment = {},
            onDoctorDetails = {
                onDoctorDetails(it)
            },
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier,
    categories: List<Category>,
    isCategoryLoading: Boolean,
    categoryError: String?,

    doctors: List<Doctor>,
    isDoctorLoading: Boolean,
    doctorError: String?,
    onDoctorDetails:(String)-> Unit,

    appointments: List<Appointment>,
    isAppointmentLoading: Boolean,
    appointmentError: String?,

    onRetryCategories: () -> Unit = {},
    onRetryDoctors: () -> Unit = {},
    onRetryAppointment:()->Unit = {}
){
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        AppointmentHeader(
            appointmentList = appointments,
            isLoading = isAppointmentLoading,
            error = appointmentError,
            onClick = {}
        )

        SpecialistHeader(
            categories = categories,
            isLoading = isCategoryLoading,
            error = categoryError,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        PopularDoctorRow(
            doctors = doctors,
            isLoading = isDoctorLoading,
            error = doctorError,
            onDoctorClick = { doctorId->
                onDoctorDetails(doctorId)
            },
            onAllDoctor = {}
        )
    }
}


