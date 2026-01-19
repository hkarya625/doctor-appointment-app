package com.example.doctor_appointment_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arya.bookmydoc.presentation.navigation.AppNavigation
import com.example.doctor_appointment_app.ui.theme.DoctorappointmentappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoctorappointmentappTheme {
                val currentUser = FirebaseAuth.getInstance().currentUser
                AppNavigation(
                    currentUser != null
                )
            }
        }
    }
}
