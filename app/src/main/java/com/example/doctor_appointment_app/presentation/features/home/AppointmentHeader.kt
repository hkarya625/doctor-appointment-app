package com.arya.bookmydoc.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arya.bookmydoc.R
import com.arya.bookmydoc.domain.model.Appointment
import com.arya.bookmydoc.domain.model.Category

@Composable
fun AppointmentHeader(
    appointmentList: List<Appointment>,
    isLoading: Boolean,
    error: String?,
    onClick: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 8.dp, end = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Today Appointments",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.TopStart)
            )
            Text(
                text = "See all",
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd),
                color = colorResource(R.color.teal)
            )
        }
        Spacer(Modifier.height(12.dp))
        when{
            isLoading -> {
                CircularIndicator()
            }
            error != null -> {
                ErrorView(
                    message = error,
                    onRetry = {

                    }
                )
            }
            appointmentList.isEmpty() -> EmptyState("No data available")
            else -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    items(appointmentList){ item->
                        AppointmentItems(item)
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentItems(item: Appointment) {
    Box(
        modifier = Modifier
            .height(120.dp)
            .width(230.dp)
            .background(Color.White, shape = RoundedCornerShape(24.dp)) // background with shape
            .clip(RoundedCornerShape(24.dp)) // optional if you want to clip content too
            .padding(12.dp)
    ) {
        Text(
            text = "Video\nConsultations",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.TopStart),
            maxLines = 2
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .size(35.dp)
                .background(colorResource(R.color.mint)),
        ){
            Image(
                painter = painterResource(R.drawable.ic_video_call),
                contentDescription = "appointment_type_logo",
                modifier = Modifier
                    .align(Alignment.Center),
                colorFilter = ColorFilter.tint(colorResource(R.color.teal))
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            Text(
                text = "Dr. Eleanor Shaw",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Timing: ",
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}
