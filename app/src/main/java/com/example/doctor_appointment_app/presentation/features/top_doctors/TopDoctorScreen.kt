package com.arya.bookmydoc.presentation.features.top_doctors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arya.bookmydoc.R
import com.arya.bookmydoc.domain.model.Category
import com.arya.bookmydoc.domain.model.Doctor
import com.arya.bookmydoc.presentation.viewmodel.HomeViewModel
import com.arya.bookmydoc.presentation.features.CommonHeader
import com.arya.bookmydoc.presentation.features.home.AppointmentHeader
import com.arya.bookmydoc.presentation.features.home.CircularIndicator
import com.arya.bookmydoc.presentation.features.home.DoctorCard
import com.arya.bookmydoc.presentation.features.home.EmptyState
import com.arya.bookmydoc.presentation.features.home.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopDoctorScreen(
    onBack:()->Unit,
    onDoctorDetails:()->Unit,
    viewModel: HomeViewModel = hiltViewModel()
    ){
    val doctorState by viewModel.doctorUiState.collectAsStateWithLifecycle()
    val categoryList = doctorState.doctors
    val isDoctorLoading = doctorState.isLoading
    val doctorError = doctorState.error

    val doctors = doctorState.doctors


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CommonHeader(
                title = "Top Doctors",
                onBack = {
                    onBack()
                },
            )
        }
    ) { paddingValues ->
        TopDoctorsContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            doctors = doctors,
            isDoctorLoading = isDoctorLoading,
            doctorError = doctorError
        )
        AppointmentHeader(
            appointmentList = emptyList(),
            isLoading = false,
            error = null,
            onClick = {}
        )
    }
}

@Composable
fun TopDoctorsContent(
    modifier: Modifier,
    doctors: List<Doctor>,
    isDoctorLoading: Boolean,
    doctorError: String?,
){
    Column(
        modifier = modifier
            .padding(horizontal = 5.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Top
    ) {

        when {
            isDoctorLoading -> {
                CircularIndicator()
            }

            doctorError != null -> {
                ErrorView(
                    message = doctorError,
                    onRetry = {}
                )
            }

            doctors.isEmpty() -> EmptyState("No doctors available")

            else -> {
                val topDoctors = doctors.filter { it.rating >= 4.5 }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(topDoctors){doctor->
                        DoctorRowCard(
                            doctorName = doctor.name,
                            specialization = doctor.specialization,
                            rating = doctor.rating,
                            imageRes = doctor.profileImage,
                            onBookClick = { },
                            onFavouriteClick = {},
                            isFavorite = true
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun DoctorRowCard(
    doctorName: String = "Dr. Emily Carter",
    specialization: String = "Dermatologist",
    rating: Double = 4.5,
    imageRes: String? = "",
    onBookClick: () -> Unit = {},
    onFavouriteClick:()->Unit = {},
    isFavorite:Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Doctor image
                Image(
                    painter = painterResource(R.drawable.doc_female2),
                    contentDescription = "doctor_dp",
                    modifier = Modifier
                        .size(width = 100.dp, height = 108.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFB2F0E9)), // mint hex color
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Doctor info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    VerifiedTag(text = "Professional Doctor")
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = doctorName,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )

                    Text(
                        text = specialization,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val starColor = Color(0xFFFFA534) // orange

                        repeat(rating.toInt()) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = starColor
                            )
                        }
                        if (rating - rating.toInt() >= 0.5) {
                            Icon(
                                imageVector = Icons.Default.StarHalf,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = starColor
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "$rating",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                val favoriteIcon = if(isFavorite) Icons.Default.Favorite else Icons.Outlined.Favorite
                Icon(
                    imageVector = favoriteIcon,
                    contentDescription = "favorite_icon",
                    tint = colorResource(R.color.teal),
                    modifier = Modifier.size(28.dp).clickable{
                        onFavouriteClick()
                    }
                )
            }

            // Button
            OutlinedButton(
                onClick = onBookClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.mint),
                    contentColor = colorResource(R.color.teal)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp,
                    brush = SolidColor(colorResource(R.color.teal))
                )
            ) {
                Text("Book Appointment")
            }
        }
    }
}


@Composable
fun VerifiedTag(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(32.dp)
            .background(colorResource(R.color.mint), shape = RoundedCornerShape(100.dp))
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_verified),
            contentDescription = "verified_tick",
            colorFilter = ColorFilter.tint(colorResource(R.color.teal))
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun PreviewTopDoctorScreen() {
//    val doctorDetails = Doctor(
//        id = 0,
//        name = "Dr. Michael Roberts",
//        specialization = "Orthopedics",
//        experience = 20,
//        patients = "1200+",
//        rating = 4.2,
//        biography = "A board-certified with over 25 years of experience, specializing in heart conditions such as coronary artery disease and arrhythmias. Known for patient-centered care and a commitment to the latest medical advancements.",
//        address = "8502 Preston Rd, Inglewood, Maine 98380",
//        mobile = "00123456789",
//        site = "http://www.test.com",
//        location = "http://maps.google.com/maps?q=loc:31.995801008207952,44.31452133516133",
//        picture = "https://firebasestorage.googleapis.com/v0/b/project198-ee047.appspot.com/o/Dr.%20Michael%20Roberts.png?alt=media&token=2d1b192d-957c-4305-9169-cac8e65310e6"
//    )
//}