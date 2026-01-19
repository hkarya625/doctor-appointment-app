package com.arya.bookmydoc.presentation.features.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arya.bookmydoc.R

@Composable
fun IntroScreen(
    onStartClick:()-> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.teal))
            .padding(16.dp)
    ) {
        Image(
            painterResource(R.drawable.doc_female),
            contentDescription = "intro_doctor",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 35.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
            contentScale = ContentScale.FillWidth
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BookMyDoc",
                fontSize = 32.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.intro_description),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.LightGray
            )
            Spacer(Modifier.height(22.dp))
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.mint),
                    contentColor = colorResource(R.color.teal)
                ),
                contentPadding = PaddingValues(start = 8.dp, end = 16.dp)
            ) {
                Text(text = stringResource(R.string.get_started), fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntroScreenPreview(){
    IntroScreen {  }
}