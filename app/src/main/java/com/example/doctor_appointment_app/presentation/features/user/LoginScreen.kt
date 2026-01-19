package com.arya.bookmydoc.presentation.features.user

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
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
import com.arya.bookmydoc.presentation.viewmodel.UserViewmodel



@Composable
fun LoginScreen(
    userViewmodel: UserViewmodel = hiltViewModel(),
    onLoginSuccess:()-> Unit,
    onSignUpClick:()-> Unit
){
    val state by userViewmodel.loginState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state) {
        when{
            state.uiStatus.isSuccess && state.user != null -> onLoginSuccess()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                WaveHeader(Modifier.fillMaxSize().align(Alignment.CenterEnd))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ){

            LoginContent(
                onLogin = { email, password ->
                    if (email.isNotBlank() && password.isNotBlank()) {
                        userViewmodel.login(email, password)
                    }
                    else {
                        Toast.makeText(context, "Email or Password can't be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                onSignUpClick = {
                    onSignUpClick()
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
fun LoginContent(
    onLogin:(String, String)-> Unit,
    onSignUpClick: () -> Unit
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Sign In",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Welcome to HealthHub, your all-in-one\nhealthcare companion!",
            color = Color.Gray
        )

        // Email
        Text(
            modifier = Modifier.padding(top = 28.dp),
            text = "Email",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            value = email,
            onValueChange = {
                email = it
            },
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

        // Password
        Text(
            modifier = Modifier.padding(top = 28.dp),
            text = "Password",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = colorResource(R.color.teal)
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedBorderColor = colorResource(R.color.teal),
                unfocusedBorderColor = Color.White
            )
        )

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp),
            text = "Forgot password",
            color = colorResource(R.color.teal)
        )

        // Login button
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.teal)
            ),
            onClick = {
               onLogin(email, password)
            }
        ) {
            Text(
                text = "Sign In",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomClickableText(
            normalText = "Don't have an account? ",
            clickableText = "Sign up",
            onClick = onSignUpClick
        )
    }
}


@Composable
fun WaveHeader(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
    ) {
        val w = size.width
        val h = size.height

        // Main wave
        val path = Path().apply {
            moveTo(0f, h * 0.3f)
            cubicTo(
                w * 0.25f, h * 0.1f,
                w * 0.75f, h * 0.5f,
                w, h * 0.25f
            )
            lineTo(w, h)       // ➜ CLOSE DOWNWARD
            lineTo(0f, h)
            close()
        }
        drawPath(path, Color(0xFF0DA574))

        // Overlay wave
        val overlay = Path().apply {
            moveTo(0f, h * 0.45f)
            cubicTo(
                w * 0.3f, h * 0.25f,
                w * 0.7f, h * 0.6f,
                w, h * 0.4f
            )
            lineTo(w, h)       // ➜ CLOSE DOWNWARD
            lineTo(0f, h)
            close()
        }
        drawPath(overlay, Color.White, alpha = 0.18f)
    }
}





@Preview(showBackground = false)
@Composable
private fun LoginScreenPreview() {
//    LoginScreen() { }
//    CurvedHeader()

//    RibbonHeader()
//    DoubleCurveHeader()
    WaveHeader()
}