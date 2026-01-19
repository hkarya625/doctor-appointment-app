package com.arya.bookmydoc.presentation.features.profile

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.arya.bookmydoc.R
import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.presentation.features.home.AppointmentItems
import com.arya.bookmydoc.presentation.features.home.CircularIndicator
import com.arya.bookmydoc.presentation.features.home.EmptyState
import com.arya.bookmydoc.presentation.features.home.ErrorView
import com.arya.bookmydoc.presentation.viewmodel.UserViewmodel

@Composable
fun ProfileScreen(
    userViewmodel: UserViewmodel = hiltViewModel(),
    onProfileClick:()->Unit,
    onLogout:()-> Unit
){

    LaunchedEffect(Unit) {
        userViewmodel.loadCurrentUser()
    }


    val profileState by userViewmodel.profileUiState.collectAsState()

    val profile = profileState.user
    val isProfileLoading = profileState.uiStatus.isLoading
    val profileError = profileState.uiStatus.errorMessage

    Scaffold(
        topBar = {
            ProfileTopBar(
                userName = profile?.name ?: "Guest User",
                email = profile?.email ?: "No email available",
                image = profile?.profileImageUrl
            )
        }
    ) { paddingValues ->
        profile?.let {
            ProfileContent(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                user = profile,
                isLoading = isProfileLoading,
                error = profileError,
                onProfileClick = {},
                onLogout = {
                    userViewmodel.logout(profile.id)
                    onLogout()
                },
            )
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier,
    user: User?,
    isLoading:Boolean,
    error: String?,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit
){
    Column(
        modifier = modifier
            .padding(top = 12.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.Gray.copy(alpha = 0.1f))
    ) {
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
            else -> {
                val userDetailsList = listOf(
                    Icons.Outlined.Info to "Profile Details",
                    Icons.Outlined.Favorite to "My Favorite",
                    Icons.Outlined.People to "Private Doctor",
                    Icons.Outlined.Logout to "Logout",
                )
                userDetailsList.forEach { (icon, title) ->
                    UserDetailItem(
                        icon,
                        title,
                        onClick = {
                            when(title){
                                "Logout"->{
                                    onLogout()
                                }
                                "Profile Details"->{
                                    onProfileClick()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun UserDetailItem(
    icon: ImageVector,
    title:String,
    onClick:()-> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable{
                onClick()
            }
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.4f))
                .padding(4.dp)
        ){
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = icon,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text =  title,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.NavigateNext,
            contentDescription = null
        )
    }
}

@Composable
fun ProfileTopBar(
    userName:String,
    email:String,
    image: String?
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize(1/3f)
            .background(
                color = colorResource(R.color.teal),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = image,
                contentDescription = "profile_image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = userName,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = email,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenTopBarPreview() {
//    ProfileScreen(){}
}