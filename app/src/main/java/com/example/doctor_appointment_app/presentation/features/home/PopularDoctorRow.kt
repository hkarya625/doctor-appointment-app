package com.arya.bookmydoc.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arya.bookmydoc.R
import com.arya.bookmydoc.domain.model.Doctor

@Composable
fun PopularDoctorRow(
    doctors:List<Doctor>,
    isLoading: Boolean,
    error: String?,
    onDoctorClick:(String)-> Unit,
    onAllDoctor:()-> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, start = 8.dp, end = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Doctors",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.TopStart)
            )
            Text(
                text = "See all",
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable{
                        onAllDoctor()
                    },
                color = colorResource(R.color.teal)
            )
        }
        when {
            isLoading -> {
                CircularIndicator()
            }

            error != null -> {
                ErrorView(
                    message = error,
                    onRetry = {}
                )
            }

            doctors.isEmpty() -> EmptyState("No doctors available")

            else -> {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(doctors){ doctor->
                        DoctorCard(
                            onDoctorClick = {onDoctorClick(doctor.id)},
                            doctorDetails = doctor
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun DoctorCard(
    onDoctorClick:()-> Unit,
    doctorDetails: Doctor
){
    Surface(
        modifier = Modifier
            .width(200.dp)
            .padding(10.dp)
            .clickable {
                onDoctorClick()
            },
        shape = RoundedCornerShape(10.dp),
        tonalElevation = 4.dp,
        color = Color.White
    ){
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(R.color.mint)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.size(124.dp).offset(y = (-8).dp),
                    model = doctorDetails.profileImage,
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    alignment = Alignment.TopCenter
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = doctorDetails.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 3.dp),
                text = doctorDetails.specialization,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                IconText(
                    icon = Icons.Outlined.Star,
                    contentDescription = "rating_star",
                    tint = Color(0xFFFFC107),
                    text = doctorDetails.rating.toString(),
                    textColor = Color.Black
                )

                // Right side — Experience
                IconText(
                    icon = Icons.Filled.Work,
                    contentDescription = "experience",
                    tint = colorResource(R.color.teal),
                    text = "${doctorDetails.experience}+ Years",
                    textColor = Color.Gray
                )
            }

            Spacer(Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(75.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(colorResource(R.color.mint)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Appointment",
                        fontSize = 10.sp,
                        color = colorResource(R.color.teal),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Timer,
                        "appointment_time",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text("10:00-02:00", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun RowScope.IconText(
    icon: ImageVector,
    contentDescription: String,
    tint: Color,
    text: String,
    textColor: Color
) {
    Row{
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewDoctor(){
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
//    DoctorCard(onDoctorClick = {},doctorDetails)

//    PopularDoctorRow(
//        title = "Title",
//        doctors = listOf(
//            doctorDetails,
//            doctorDetails
//        ),
//        isLoading = false,
//        error = null,
//        onDoctorClick = {  },
//    )
}