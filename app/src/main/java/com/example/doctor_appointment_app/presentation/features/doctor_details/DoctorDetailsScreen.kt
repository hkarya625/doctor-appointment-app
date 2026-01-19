package com.arya.bookmydoc.presentation.features.doctor_details

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arya.bookmydoc.R
import com.arya.bookmydoc.data.mapper.toDoctorDetails
import com.arya.bookmydoc.domain.model.Appointment
import com.arya.bookmydoc.domain.model.AppointmentStatus
import com.arya.bookmydoc.domain.model.DoctorContactDetails
import com.arya.bookmydoc.domain.model.Doctor
import com.arya.bookmydoc.presentation.viewmodel.AppointmentViewModel
import com.arya.bookmydoc.presentation.features.CommonHeader
import com.arya.bookmydoc.presentation.features.appointment.PaymentSuccessScreen
import com.arya.bookmydoc.presentation.features.appointment.ScheduleAppointment
import com.arya.bookmydoc.presentation.viewmodel.ContactAction
import com.arya.bookmydoc.presentation.viewmodel.HomeViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DoctorDetailsScreen(
    doctorId: String,
    onBack: () -> Unit,
    onWebsite: (String) -> Unit,
    onSendSms: (String, String) -> Unit,
    onDial: (String) -> Unit,
    onDirection: (String) -> Unit,
    onShare: (String, String) -> Unit,
    onGoHome: () -> Unit,
    onViewAppointment: () -> Unit,
    appointmentViewModel: AppointmentViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by appointmentViewModel.bookingUiState.collectAsStateWithLifecycle()
    val doctorState by homeViewModel.doctorUiState.collectAsStateWithLifecycle()

    val doctor = doctorState.doctors.find { it.id == doctorId }

    val isBooked = uiState.isBooked
    val isLoading = uiState.isBooked
    val error = uiState.error

    var date by remember { mutableStateOf<LocalDate?>(null) }
    var time by remember { mutableStateOf<LocalTime?>(null) }

    Scaffold(
        topBar = {
            CommonHeader(
                title = "Doctor Screen",
                onBack = {onBack()}
            )
        },
        bottomBar = {
            doctor?.let {
                AppointmentButton(
                    isEnable = date != null && time != null,
                    onClick = {
                        date?.let { d->
                            time?.let { t->

                                Log.d("AppointmentBooked", "$date, $time")
                                appointmentViewModel.bookAppointment(
                                    doctorId = doctor.id,
                                    doctorName = doctor.name,
                                    date = d,
                                    time = t
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        doctor?.let {
            DoctorDetailsContent(
                modifier = Modifier.padding(paddingValues),
                doctor = doctor,
                onDateSelected = { date = it },
                onTimeSelected = { time = it },
                onContact = { action ->
                    when (action) {
                        ContactAction.Call -> doctor.phoneNumber.let(onDial)
                        ContactAction.Sms -> doctor.phoneNumber.let { onSendSms(it, "Hello Doctor") }
                        ContactAction.Website -> doctor.site?.let(onWebsite)
                        ContactAction.Share -> onShare(
                            doctor.name,
                            "${doctor.name}, ${doctor.address}, ${doctor.phoneNumber}"
                        )
                    }
                }
            )
        }
    }

    // ------------------ Dialog Handler ------------------
    if (uiState.showDialog) {
        Dialog(onDismissRequest = { appointmentViewModel.dismissDialog() }) {

            AnimatedContent(
                targetState = uiState.isLoading to uiState.isBooked,
                transitionSpec = { fadeIn() with fadeOut() }
            ) { (loading, booked) ->
                when {
                    loading -> BookingLoadingContent()
                    booked -> PaymentSuccessScreen(
                        onViewAppointment = onViewAppointment,
                        onGoHome = onGoHome,
                        doctorName = doctor?.name ?: "",
                        slot = "$date $time"
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// CONTENT
// -----------------------------------------------------------------------------------------

@Composable
fun DoctorDetailsContent(
    modifier: Modifier,
    doctor: Doctor,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
    onContact: (ContactAction) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DoctorProfile(
            name = doctor.name,
            specialization = doctor.specialization,
            rating = doctor.rating,
            address = doctor.address,
            experience = doctor.experience,
            patients = doctor.totalPatients.toString(),
            profileUrl = doctor.profileImage.toString()
        )
        Text(doctor.name, fontSize = 26.sp, modifier = Modifier.padding(8.dp))
        Text(doctor.specialization, modifier = Modifier.padding(8.dp))

        Biography("Biography", doctor.biography)

        ContactOptions(doctor = doctor, onContact = onContact)

        ScheduleAppointment(
            onDateSelected = {
                it?.let {
                    onDateSelected(it)
                }
            },
            onTimeSelected = {
                it?.let {
                    onTimeSelected(it)
                }
            },
        )
    }
}

// -----------------------------------------------------------------------------------------
// CONTACT OPTIONS
// -----------------------------------------------------------------------------------------

@Composable
fun ContactOptions(
    doctor: Doctor,
    onContact: (ContactAction) -> Unit
) {
    val options = listOf(
        ContactAction.Call to !doctor.phoneNumber.isNullOrBlank(),
        ContactAction.Sms to !doctor.phoneNumber.isNullOrBlank(),
        ContactAction.Website to !doctor.site.isNullOrBlank(),
        ContactAction.Share to true
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        items(options) { (action, enabled) ->

            val icon = when (action) {
                ContactAction.Call -> R.drawable.ic_call
                ContactAction.Sms -> R.drawable.ic_chat_outlined
                ContactAction.Website -> R.drawable.ic_website
                ContactAction.Share -> R.drawable.ic_share
            }

            IconButton(
                enabled = enabled,
                onClick = { onContact(action) }
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = colorResource(R.color.teal)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// UI COMPONENTS
// -----------------------------------------------------------------------------------------

@Composable
fun Biography(header: String, description: String) {
    Column(Modifier.padding(12.dp)) {
        Text(header, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            description,
            color = Color.Gray,
            textAlign = TextAlign.Justify
        )
    }
}


@Composable
fun BookingLoadingContent() {
    Card(
        modifier = Modifier.padding(12.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(Modifier.height(12.dp))
            Text("Booking your appointment...")
        }
    }
}


@Composable
fun AppointmentButton(
    isEnable: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnable,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.teal)
        )
    ) {
        Text("Book Appointment", fontSize = 18.sp)
    }
}
