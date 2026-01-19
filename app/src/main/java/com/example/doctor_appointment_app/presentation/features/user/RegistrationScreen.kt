package com.arya.bookmydoc.presentation.features.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arya.bookmydoc.R
import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.domain.model.UserRole
import com.arya.bookmydoc.presentation.viewmodel.RegisterUiState
import com.arya.bookmydoc.presentation.viewmodel.UserViewmodel

@Composable
fun SignupScreen(
    userViewmodel: UserViewmodel = hiltViewModel(),
    onRegistrationSuccess: () -> Unit,
    onLoginClick: () -> Unit
){
    val state by userViewmodel.registerState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Navigate once only
    LaunchedEffect(state.uiStatus.isSuccess) {
        if (state.uiStatus.isSuccess) {
            Toast.makeText(context, "You have successfully registered", Toast.LENGTH_SHORT).show()
            onRegistrationSuccess()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { WaveHeader(Modifier.fillMaxSize().align(Alignment.CenterEnd)) }
        }
    ) { padding ->

        Box(Modifier.fillMaxSize().padding(padding)) {

            // 👇 Main UI content
            SignupContent(
                onLoginClick = onLoginClick,
                onSignup = { email, password, name ->
                    userViewmodel.register(
                        user = User(
                            id = "",
                            name = name,
                            email = email,
                            phoneNumber = "",
                            age = 0,
                            gender = "M",
                            profileImageUrl = "",
                            role = UserRole.PATIENT,
                            appointments = emptyList()
                        ),
                        email = email,
                        password = password
                    )
                }
            )

            // 👇 Loading overlay (recommended)
            if (state.uiStatus.isLoading) {
                LoadingIndicator()
            }

            // 👇 Error overlay (recommended)
            state.uiStatus.errorMessage?.let {
                ErrorMessage(it)
            }
        }
    }
}
@Composable
fun SignupContent(
    onLoginClick: () -> Unit,
    onSignup: (email: String, password: String, name: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(18.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "Sign Up",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Welcome to HealthHub. your all-in-one\nhealthcare companion!",
            color = Color.Gray
        )

        // Name Field
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = "Name",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        val dColor = colorResource(R.color.teal)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedBorderColor = colorResource(R.color.teal),
                unfocusedBorderColor = Color.White
            )
        )

        // Email Field
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = "Email",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedBorderColor = colorResource(R.color.teal),
                unfocusedBorderColor = Color.White
            )
        )

        // Password Field
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = "Password",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = dColor
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedBorderColor = colorResource(R.color.teal),
                unfocusedBorderColor = Color.White
            )
        )

        // Signup Button
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                onSignup(email, password, name)  // 🔥 passes values to parent
            },
            colors = ButtonDefaults.buttonColors(containerColor = dColor)
        ) {
            Text(
                text = "Sign Up",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Login Redirect
        CustomClickableText(
            normalText = "Already have an account? ",
            clickableText = "Sign in",
            onClick = { onLoginClick() }
        )
    }
}

@Composable
fun ErrorMessage(error: String) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = error,
            color = Color.Red
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            color = colorResource(R.color.teal)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationScreenPreview() {
//    RegistrationScreen {  }
    LoadingIndicator()
}