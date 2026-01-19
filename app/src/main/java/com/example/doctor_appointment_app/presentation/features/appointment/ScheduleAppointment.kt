package com.arya.bookmydoc.presentation.features.appointment

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.arya.bookmydoc.R
import com.arya.bookmydoc.presentation.viewmodel.AppointmentViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale
@Composable
fun ScheduleAppointment(
    onDateSelected: (LocalDate?) -> Unit,
    onTimeSelected: (LocalTime?) -> Unit,
    appointmentViewModel: AppointmentViewModel = hiltViewModel()
) {
    var today by remember { mutableStateOf(LocalDate.now()) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    // Update date & time every minute
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000)
            today = LocalDate.now()
            currentTime = LocalTime.now()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Schedule",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(5.dp))

        /* -------------------- DATE SELECTION -------------------- */

        val dates = remember(today) {
            (0..10).map { today.plusDays(it.toLong()) }
        }

        var selectedDate by remember(today) { mutableStateOf(today) }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dates) { date ->
                AppointmentItem(
                    isSelected = selectedDate == date,
                    onSelected = {
                        selectedDate = date
                        onDateSelected(date)
                    },
                    list = listOf(
                        date.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        ),
                        "${date.dayOfMonth} ${
                            date.month.getDisplayName(
                                TextStyle.SHORT,
                                Locale.getDefault()
                            )
                        }"
                    )
                )
            }
        }

        /* -------------------- TIME SELECTION -------------------- */

        Text(
            modifier = Modifier.padding(top = 18.dp),
            text = "Choose Time",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(5.dp))

        val slots = remember(currentTime) {
            val roundedHour =
                if (currentTime.minute >= 50) currentTime.hour + 1 else currentTime.hour

            val startHour = if (selectedDate == today) maxOf(8, roundedHour) else 8
            val endHour = 20

            if (startHour > endHour) {
                emptyList()
            } else {
                (startHour..endHour).map { hour ->
                    LocalTime.of(hour, 0)
                }
            }
        }

        Log.d("slots", slots.toString())

        var selectedTime by remember(slots) {
            mutableStateOf(slots.firstOrNull())
        }

        // Keep selection valid when slots update
        LaunchedEffect(slots) {
            if (selectedTime !in slots) {
                selectedTime = slots.firstOrNull()
                onTimeSelected(selectedTime)
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(slots) { slot ->
                AppointmentItem(
                    isSelected = slot == selectedTime,
                    onSelected = {
                        selectedTime = slot
                        onTimeSelected(slot)
                    },
                    list = listOf(slot.toString())
                )
            }
        }
    }
}


@Composable
fun AppointmentItem(
    isSelected: Boolean,
    onSelected:()-> Unit,
    list:List<String>
){
    val boxColor = if(isSelected) colorResource(R.color.teal) else Color.White
    val textColor = if(isSelected) Color.White else Color.Black
    Column(
        modifier = Modifier
            .width(70.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(boxColor)
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .clickable {
                onSelected()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        list.forEach {
            Text(
                text = it,
                color = textColor
            )
        }
    }
}


@Composable
fun PaymentSuccessScreen(
    doctorName:String,
    slot:String,
    status:String = "Booked",
    amount:String = "200",
    onViewAppointment: () -> Unit,
    onGoHome: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ✅ Top success icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_verified), // ✅ your success icon
                    contentDescription = "Success",
                    tint = colorResource(R.color.teal),
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Title
            Text(
                text = "Booking Successful !",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Subtitle
            Text(
                text = "Congratulations! You have booked\nappointment with $doctorName",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Payment details
            PaymentDetailRow("Payment Time", slot)
            PaymentDetailRow("Amount", "Rs.$amount")
            PaymentDetailRow("Payment Methods", "PayPal")
            PaymentDetailRow("Status", status, valueColor = Color(0xFF4CAF50))

            Spacer(modifier = Modifier.height(24.dp))

            // ✅ Buttons
            Button(
                onClick = { onViewAppointment() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.teal),)
            ) {
                Text(text = "View Appointment", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { onGoHome() }) {
                Text(text = "Go To Home", color = Color.Black)
            }
        }
    }
}

@Composable
fun PaymentDetailRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}



@Preview(showBackground = true)
@Composable
private fun PreviewScheduleAppointment() {
//    PaymentSuccessScreen()
}