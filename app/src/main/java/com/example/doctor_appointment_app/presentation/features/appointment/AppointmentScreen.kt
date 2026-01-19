package com.arya.bookmydoc.presentation.features.appointment

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.arya.bookmydoc.R
import com.arya.bookmydoc.domain.model.AppointmentStatus
import com.arya.bookmydoc.presentation.viewmodel.AppointmentViewModel
import com.arya.bookmydoc.presentation.features.CommonHeader
import com.arya.bookmydoc.presentation.features.home.CircularIndicator
import com.arya.bookmydoc.presentation.viewmodel.UserViewmodel
import com.arya.bookmydoc.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    appointmentViewModel: AppointmentViewModel = hiltViewModel(),
    userViewmodel: UserViewmodel = hiltViewModel()
) {

    val userState by userViewmodel.profileUiState.collectAsStateWithLifecycle()
    val appointmentsState by appointmentViewModel.appointmentUiState.collectAsStateWithLifecycle()

    // Load appointments after user data loads
    LaunchedEffect(Unit) {
        appointmentViewModel.loadAppointments()
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Upcoming", "Previous")

    Scaffold(
        topBar = {
            CommonHeader(
                title = "Appointments",
                onBack = {}
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 5.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {

            // ------------------ Tabs ------------------
            TabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        selectedContentColor = colorResource(R.color.teal),
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // ------------------ Loading ------------------
            when {
                appointmentsState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularIndicator()
                    }
                }

                // ------------------ Error ------------------
                appointmentsState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = appointmentsState.error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                else -> {
                    Log.d("AppointmentList", "${appointmentsState.appointments}")
                    val filteredAppointments = appointmentsState.appointments.filter { appt ->
                        if (selectedTabIndex == 0)
                            appt.status == AppointmentStatus.PENDING || appt.status == AppointmentStatus.CONFIRMED
                        else
                            appt.status == AppointmentStatus.COMPLETED || appt.status == AppointmentStatus.CANCELLED
                    }

                    // ------------------ Empty State ------------------
                    if (filteredAppointments.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No appointments")
                        }
                    } else {
                        // ------------------ List ------------------
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredAppointments) { appointment ->
                                AppointmentCard(
                                    doctor = appointment.doctorName,
                                    status = appointment.status.toString(),
                                    date = appointment.dateTime.toLocalDate().toString(),
                                    time = appointment.dateTime.toLocalTime().toString(),
                                    onclick = { }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AppointmentCard(
    doctor:String,
    status:String,
    date:String,
    time:String,
    onclick: () -> Unit = {})
{

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .clickable { onclick() }
            .background(Color.White)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 5.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.2f))
                    .align(Alignment.CenterStart)
            ){
                AsyncImage(
                    model = "https://i.pinimg.com/736x/d7/64/18/d76418f406971b8b0c02d158e159d920.jpg",
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.width(35.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Center)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = doctor,
                    style = Typography.titleMedium
                )
                TextWithIcon(
                    Icons.Default.CalendarMonth,
                    date
                )
                TextWithIcon(
                    Icons.Default.WatchLater,
                    time
                )
            }

            val statusColor = when(status){
                "booked"->Color(0xFFFFC107)
                "completed" ->Color(0xFF009E60)
                "cancelled" -> Color(0xFFDE4B4B)
                else -> Color(0xFF3F51B5)
            }
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(statusColor.copy(alpha = 0.2f))
                    .height(20.dp)
                    .wrapContentWidth() // ensures Box width fits content
                    .padding(horizontal = 4.dp) // adds inner padding for text
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = status,
                    color = statusColor,
                    fontSize = 12.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TextWithIcon(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.height(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = icon,
            contentDescription = "",
            colorFilter = ColorFilter.tint(Color.DarkGray)
        )
        Spacer(Modifier.size(5.dp))
        Text(
            text = text,
            color = Color.DarkGray,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppointmentScreenPreview() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(4){ appointment->
            AppointmentCard(
                doctor = "Dr. Kuldeep Singh",
                status = "booked",
                date = "12 Jan 2025",
                time = "04:30PM",
                onclick = {}
            )
        }
    }

}