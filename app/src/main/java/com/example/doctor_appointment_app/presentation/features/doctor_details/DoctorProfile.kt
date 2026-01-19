package com.arya.bookmydoc.presentation.features.doctor_details

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arya.bookmydoc.R

@Composable
fun DoctorProfile(
    name: String,
    specialization: String,
    rating: Double,
    address: String,
    experience: Int,
    patients: String,
    profileUrl:String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        // --- Profile Image ---
        AsyncImage(
            model = profileUrl,
            contentDescription = "profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(140.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
        )

        Spacer(Modifier.width(12.dp))

        // --- Info Section ---
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            RatingBar(rating = rating)

            Spacer(Modifier.height(6.dp))
            Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = specialization, fontSize = 16.sp, color = Color.Gray)

            Spacer(Modifier.height(10.dp))
            InfoRow(
                icon = Icons.Outlined.LocationOn,
                tint = Color.Gray,
                text = address
            )

            Spacer(Modifier.height(8.dp))
            InfoRow(
                icon = Icons.Filled.Work,
                tint = colorResource(R.color.teal),
                label = "Experience:",
                value = "$experience years"
            )

            Spacer(Modifier.height(8.dp))
            InfoRow(
                icon = Icons.Filled.People,
                tint = colorResource(R.color.teal),
                label = "Patients:",
                value = patients
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun RatingBar(rating: Double, maxRating: Int = 5) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val fullStars = rating.toInt()
        val hasHalfStar = rating - fullStars >= 0.5

        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "star",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(20.dp)
            )
        }
        if (hasHalfStar) {
            Icon(
                imageVector = Icons.Default.StarHalf,
                contentDescription = "half star",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(20.dp)
            )
        }
        if (fullStars < maxRating - 1) {
            repeat(maxRating - fullStars - if (hasHalfStar) 1 else 0) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "empty star",
                    tint = Color(0xFFDADADA),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.width(4.dp))
        Text(text = String.format("%.1f", rating), fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    tint: Color,
    text: String? = null,
    label: String? = null,
    value: String? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(6.dp))
        when {
            label != null && value != null -> {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Gray)) { append("$label ") }
                        withStyle(SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                            append(value)
                        }
                    },
                    fontSize = 14.sp
                )
            }
            text != null -> {
                Text(text = text, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}
