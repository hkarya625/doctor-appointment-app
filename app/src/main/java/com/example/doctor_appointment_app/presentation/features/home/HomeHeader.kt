package com.arya.bookmydoc.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import coil.compose.AsyncImage
import com.arya.bookmydoc.R



@Composable
fun HomeHeader(
    name:String,
    photo:String?,
    onProfileClick:()-> Unit,
    onBellClick: () -> Unit,
    onSearchClick:(String)-> Unit,
    onMicClick: () -> Unit
    ){

    var searchText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            )
            .background(color = colorResource(R.color.teal))
            .padding(start = 12.dp, end = 12.dp, top = 45.dp, bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            if (photo != null) {
                AsyncImage(
                    model = photo,
                    contentDescription = "user_profile",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable { onProfileClick() },
                    contentScale = ContentScale.Crop
                )
            } else {
                // Show default placeholder (initials, icon, or blank image)
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "default_profile",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable { onProfileClick() }
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "How are you today?",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.7f).padding(top = 5.dp)
                )
            }

            IconButton(
                onClick = {
                    onBellClick
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    imageVector = Icons.Default.Notifications,
                    tint = Color.White,
                    contentDescription = "Notifications"
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onSearchClick(it)
            },
            placeholder = {
                Text(text = "Search doctor in your city...")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search_icon")
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onMicClick
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.3f))
                ) {
                    Image(
                        painterResource(R.drawable.ic_mic),
                        contentDescription = "mic_icon",
                        colorFilter = ColorFilter.tint(colorResource(R.color.teal))
                    )
                }
            },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = TextFieldDefaults.colors()
        )
    }
}